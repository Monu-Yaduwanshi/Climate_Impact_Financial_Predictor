package com.example.climatexpert.DrawerContent

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.climatexpert.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    navController: NavHostController,
    onPaymentComplete: () -> Unit,
    totalAmount: Int,
    firebaseRepository: FirebaseRepository = FirebaseRepository()
) {
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }

    fun onPaymentComplete(paymentMethod: String) {
        // Get current cart
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firebaseRepository.getUserCartRef().get().addOnSuccessListener { snapshot ->
            val cart = snapshot.getValue(UserCart::class.java)
            cart?.let {
                // Create order
                val orderId = firebaseRepository.getOrdersRef().push().key ?: return@addOnSuccessListener
                val order = Order(
                    orderId = orderId,
                    userId = userId,
                    items = cart.items,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod
                )

                // Save order
                firebaseRepository.getOrdersRef().child(orderId).setValue(order)

                // Clear cart
                firebaseRepository.getUserCartRef().removeValue()

                // Navigate back
                navController.popBackStack("insuranceList", inclusive = false)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFA880D)))
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Display total amount
                    Text(
                        text = "Total Amount: ₹$totalAmount",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Rest of your existing PaymentContent implementation
                    PaymentContent(
                        selectedPaymentMethod = selectedPaymentMethod,
                        onPaymentMethodSelected = { selectedPaymentMethod = it },
                        onPaymentComplete = { onPaymentComplete(selectedPaymentMethod) }
                    )
                }
            }
        }
    )
}

@Composable
fun PaymentContent(
    selectedPaymentMethod: String,
    onPaymentMethodSelected: (String) -> Unit,
    onPaymentComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Payment Method",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PaymentMethodOptions(
            selectedPaymentMethod = selectedPaymentMethod,
            onPaymentMethodSelected = onPaymentMethodSelected
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (selectedPaymentMethod) {
            "UPI" -> UpiOptions()
            "Card" -> CardOptions()
            "Cash" -> CashOnDeliveryOption()
        }

        Spacer(modifier = Modifier.weight(1f))

        PlaceOrderButton(onPaymentComplete)
    }
}

@Composable
fun PlaceOrderButton(onPaymentComplete: () -> Unit) {
    Button(
        onClick = onPaymentComplete,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D47A1)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Place Order",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun PaymentMethodOptions(
    selectedPaymentMethod: String,
    onPaymentMethodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PaymentOptionCard(
            title = "UPI",
            iconRes = R.drawable.bhimupi,
            selected = selectedPaymentMethod == "UPI",
            onClick = { onPaymentMethodSelected("UPI") }
        )

        PaymentOptionCard(
            title = "Card",
            iconRes = R.drawable.cardoption,
            selected = selectedPaymentMethod == "Card",
            onClick = { onPaymentMethodSelected("Card") }
        )

        PaymentOptionCard(
            title = "Cash",
            iconRes = R.drawable.cashoption,
            selected = selectedPaymentMethod == "Cash",
            onClick = { onPaymentMethodSelected("Cash") }
        )
    }
}

@Composable
fun PaymentOptionCard(
    title: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFF0D47A1) else Color(0xFFBBDEFB)
    val contentColor = if (selected) Color.White else Color(0xFF0D47A1)

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, color = contentColor, fontSize = 16.sp)
    }
}

@Composable
fun UpiOptions() {
    var selectedUpi by remember { mutableStateOf("Paytm") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        UpiOptionRow("Paytm", R.drawable.paytm, selectedUpi == "Paytm") { selectedUpi = "Paytm" }
        UpiOptionRow("Google Pay", R.drawable.gpay, selectedUpi == "Google Pay") { selectedUpi = "Google Pay" }
        UpiOptionRow("PhonePe", R.drawable.phonepe, selectedUpi == "PhonePe") { selectedUpi = "PhonePe" }
    }
}

@Composable
fun UpiOptionRow(name: String, iconRes: Int, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, if (selected) Color(0xFFFF6D00) else Color.Gray)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = name, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, fontSize = 18.sp)
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFF6D00),
                unselectedColor = Color.Gray
            )
        )
    }
}

@Composable
fun CardOptions() {
    var selectedCardType by remember { mutableStateOf("Debit") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CardOptionRow("Debit", R.drawable.debitcard, selectedCardType == "Debit") {
            selectedCardType = "Debit"
        }

        CardOptionRow("Credit", R.drawable.creditcard, selectedCardType == "Credit") {
            selectedCardType = "Credit"
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.bank),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )
            Text("Add your card", fontSize = 18.sp)
        }
    }
}

@Composable
fun CardOptionRow(name: String, iconRes: Int, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, if (selected) Color(0xFFFF6D00) else Color.Gray)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = name, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, fontSize = 18.sp)
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFF6D00),
                unselectedColor = Color.Gray
            )
        )
    }
}

@Composable
fun CashOnDeliveryOption() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFFF6D00))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.cashoption),
                    contentDescription = "Cash On Delivery",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Cash On Delivery", fontSize = 18.sp)
            }

            RadioButton(
                selected = true,
                onClick = {},
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFF6D00),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}