package com.hafizuddin.nomadwealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NomadWealthTheme {
                NomadWealthApp()
            }
        }
    }
}

private val AppColors = darkColorScheme(
    primary = Color(0xFF18C6B5),
    secondary = Color(0xFF56A8FF),
    background = Color(0xFF07151D),
    surface = Color(0xFF0E202A),
    error = Color(0xFFFF5C67)
)

@Composable
private fun NomadWealthTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}

private enum class Screen {
    LANDING,
    APP
}

private enum class AppTab(
    val title: String,
    val icon: ImageVector
) {
    DASHBOARD("Dashboard", Icons.Default.Home),
    ACCOUNTS("Accounts", Icons.Default.CreditCard),
    TRANSACTIONS("Transactions", Icons.Default.SwapHoriz),
    LOANS("Loans", Icons.Default.AccountBalance),
    PROFILE("Profile", Icons.Default.Person)
}

@Composable
private fun NomadWealthApp() {
    var screen by remember {
        mutableStateOf(Screen.LANDING)
    }

    when (screen) {
        Screen.LANDING -> {
            LandingScreen(
                onContinue = {
                    screen = Screen.APP
                }
            )
        }

        Screen.APP -> {
            MainApp()
        }
    }
}

@Composable
private fun LandingScreen(
    onContinue: () -> Unit
) {
    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF06131C),
            Color(0xFF07333B),
            Color(0xFF06131C)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = Color(0xFF18C6B5),
                modifier = Modifier.size(42.dp)
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column {
                Text(
                    text = "Nomad Wealth",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Your money. Anywhere.",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(44.dp)
        )

        Surface(
            color = Color(0xFF18C6B5).copy(alpha = 0.15f),
            shape = CircleShape
        ) {
            Text(
                text = "PERSONAL FINANCE ACROSS COUNTRIES",
                color = Color(0xFF60E8DC),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Icon(
            imageVector = Icons.Default.Explore,
            contentDescription = null,
            tint = Color(0xFF18C6B5),
            modifier = Modifier.size(90.dp)
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Understand your money,\nwherever life takes you.",
            color = Color.White,
            fontSize = 37.sp,
            lineHeight = 43.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Track accounts, income, expenses, budgets, loans and currencies from one simple Android app.",
            color = Color.White.copy(alpha = 0.72f),
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        FeatureCard(
            title = "Multi-currency accounts",
            description = "Manage money across countries and currencies."
        )

        FeatureCard(
            title = "Money In and Money Out",
            description = "Record income and expenses through a clear dashboard."
        )

        FeatureCard(
            title = "Loans and installments",
            description = "Track payments, balances and remaining debt."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Open demonstration",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Continue",
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Nomad Wealth · Developed by Hafizuddin",
            color = Color.White.copy(alpha = 0.45f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String
) {
    Surface(
        color = Color.White.copy(alpha = 0.06f),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun MainApp() {
    var selectedTab by remember {
        mutableStateOf(AppTab.DASHBOARD)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                AppTab.DASHBOARD -> DashboardScreen()
                AppTab.ACCOUNTS -> SimpleScreen(
                    title = "Accounts",
                    message = "Your financial accounts will appear here."
                )

                AppTab.TRANSACTIONS -> SimpleScreen(
                    title = "Transactions",
                    message = "Your Money In and Money Out records will appear here."
                )

                AppTab.LOANS -> SimpleScreen(
                    title = "Loans",
                    message = "Your loans and installment information will appear here."
                )

                AppTab.PROFILE -> SimpleScreen(
                    title = "Profile",
                    message = "Your name, email and application settings will appear here."
                )
            }
        }
    }
}

@Composable
private fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Text(
            text = "Dashboard",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "GOOD EVENING",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Hafizuddin",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Here is your financial overview",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DashboardCard(
                title = "Income",
                value = "€600.00",
                valueColor = Color(0xFF25CF69),
                modifier = Modifier.weight(1f)
            )

            DashboardCard(
                title = "Expenses",
                value = "€448.00",
                valueColor = Color(0xFFFF4D59),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        DashboardCard(
            title = "Remaining this month",
            value = "€152.00",
            valueColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Text(
            text = "Recent activity",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ActivityRow(
            title = "Freelance payment",
            subtitle = "Income",
            value = "+ €600.00",
            color = Color(0xFF25CF69)
        )

        ActivityRow(
            title = "Rent",
            subtitle = "Housing",
            value = "− €400.00",
            color = Color(0xFFFF4D59)
        )

        ActivityRow(
            title = "Groceries",
            subtitle = "Food",
            value = "− €48.00",
            color = Color(0xFFFF4D59)
        )
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )

            Text(
                text = value,
                color = valueColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ActivityRow(
    title: String,
    subtitle: String,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SimpleScreen(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(22.dp)
            )
        }
    }
}
