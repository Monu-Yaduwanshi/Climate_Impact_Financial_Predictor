package com.example.climatexpert.DrawerContent

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class InsuranceItem(
    val id: Int = 0,
    val name: String = "",
    val price: Int = 0,
    val coverage: String = "",
    val description: String = "",
    val climateRisk: String = "",
    val waitingPeriod: Int = 15
) {
    constructor() : this(0, "", 0, "", "", "", 15)
}
//data class InsuranceItem(
//    val id: Int,
//    val name: String,
//    val price: Int,
//    val coverage: String,
//    val description: String,
//    val climateRisk: String,  // Specific climate risk covered
//    val waitingPeriod: Int = 15  // Days before coverage starts
//)

object InsuranceRepository {
    fun getBusinessInsuranceItems(): List<InsuranceItem> {
        return listOf(
            InsuranceItem(
                1,
                "Flood Protection Package",
                125000,
                "Property damage, Business interruption",
                "Covers flood damage to commercial properties and lost income",
                "Flooding"
            ),
            InsuranceItem(
                2,
                "Wildfire Business Shield",
                98000,
                "Property damage, Equipment replacement",
                "Protection against wildfire damage to business premises",
                "Wildfires"
            ),
            InsuranceItem(
                3,
                "Hurricane Resilience Plan",
                145000,
                "Structural damage, Inventory loss",
                "Comprehensive coverage for hurricane-prone areas",
                "Hurricanes"
            ),
            // Continue with 32 more items...
            InsuranceItem(
                4,
                "Drought Impact Coverage",
                75000,
                "Water-dependent business loss",
                "For agriculture and water-intensive industries",
                "Drought"
            ),
            InsuranceItem(
                5,
                "Sea Level Rise Protection",
                110000,
                "Coastal property damage",
                "Specialized for businesses in coastal regions",
                "Sea level rise"
            ),
            InsuranceItem(
                6,
                "Extreme Heat Business Plan",
                65000,
                "Equipment failure, Productivity loss",
                "Covers heat-related business disruptions",
                "Heat waves"
            ),
            InsuranceItem(
                7,
                "Climate-Ready Retail Package",
                85000,
                "Inventory spoilage, Store damage",
                "For retail businesses facing climate risks",
                "Multiple hazards"
            ),
            InsuranceItem(
                8,
                "Agricultural Resilience Plan",
                95000,
                "Crop failure, Livestock protection",
                "Farmers and agribusiness coverage",
                "Extreme weather"
            ),
            InsuranceItem(
                9,
                "Renewable Energy Protector",
                135000,
                "Solar/wind farm damage",
                "For green energy businesses",
                "Severe storms"
            ),
            InsuranceItem(
                10,
                "Small Business Climate Shield",
                55000,
                "Basic climate risk coverage",
                "Affordable protection for SMEs",
                "Multiple hazards"
            ),
            // Continue the pattern up to 35 items...
            InsuranceItem(
                35,
                "Comprehensive Climate Coverage",
                225000,
                "All climate risks, Full business protection",
                "Premium package for maximum security",
                "All climate risks"
            )
        ).sortedBy { it.price }  // Sort by price for better display
    }
}