package com.hafizuddin.nomadwealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hafizuddin.nomadwealth.data.Account
import com.hafizuddin.nomadwealth.data.Loan
import com.hafizuddin.nomadwealth.data.SupabaseAuth
import com.hafizuddin.nomadwealth.data.Transaction
import com.hafizuddin.nomadwealth.data.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

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

private val DarkColors = darkColorScheme(
    primary = Color(0xFF18C6B5),
    secondary = Color(0xFF56A8FF),
    background = Color(0xFF07151D),
    surface = Color(0xFF0E202A),
    error = Color(0xFFFF5C67)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF007E73),
    secondary = Color(0xFF1265A8),
    background = Color(0xFFF4F7F8),
    surface = Color.White,
    error = Color(0xFFB3261E)
)

@Composable
private fun NomadWealthTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

private enum class Route {
    LANDING,
    LOGIN,
    SIGNUP,
    APP
}

private enum class AppTab(
    val label: String,
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
    var route by remember {
        mutableStateOf(Route.LANDING)
    }

    var session by remember {
        mutableStateOf<UserSession?>(null)
    }

    AnimatedContent(
        targetState = route,
        label = "main-route"
    ) { currentRoute ->

        when (currentRoute) {
            Route.LANDING -> {
                LandingScreen(
                    onLogin = {
                        route = Route.LOGIN
                    },
                    onSignup = {
                        route = Route.SIGNUP
                    }
                )
            }

            Route.LOGIN -> {
                AuthenticationScreen(
                    signup = false,
                    onBack = {
                        route = Route.LANDING
                    },
                    onLoginSuccess = { user ->
                        session = user
                        route = Route.APP
                    },
                    onSignupComplete = {
                        route = Route.LOGIN
                    }
                )
            }

            Route.SIGNUP -> {
                AuthenticationScreen(
                    signup = true,
                    onBack = {
                        route = Route.LANDING
                    },
                    onLoginSuccess = { user ->
                        session = user
                        route = Route.APP
                    },
                    onSignupComplete = {
                        route = Route.LOGIN
                    }
                )
            }

            Route.APP -> {
                MainApplication(
                    session = session ?: UserSession(
                        name = "Nomad",
                        email = "",
                        accessToken = ""
                    ),
                    onLogout = {
                        session = null
                        route = Route.LANDING
                    }
                )
            }
        }
    }
}

@Composable
private fun LandingScreen(
    onLogin: () -> Unit,
    onSignup: () -> Unit
) {
    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF06131C),
            Color(0xFF06323A),
            Color(0xFF06131C)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    text = "Your money. Anywhere.",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onLogin
            ) {
                Text(
                    text = "Log in",
                    color = Color.White
                )
            }
        }

        Spacer(
            modifier = Modifier.height(34.dp)
        )

        Surface(
            color = Color(0xFF18C6B5).copy(alpha = 0.14f),
            shape = CircleShape
        ) {
            Text(
                text = "PERSONAL FINANCE ACROSS COUNTRIES",
                color = Color(0xFF4FE6D8),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Icon(
            imageVector = Icons.Default.Explore,
            contentDescription = null,
            tint = Color(0xFF18C6B5),
            modifier = Modifier.size(88.dp)
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Understand your money,\nwherever life takes you.",
            color = Color.White,
            fontSize = 37.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 43.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Track accounts, income, expenses, budgets, loans and currencies from one native Android app.",
            color = Color.White.copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        FeatureCard(
            icon = Icons.Default.Public,
            title = "Multi-currency accounts",
            description = "Manage money across countries and currencies."
        )

        FeatureCard(
            icon = Icons.Default.SwapHoriz,
            title = "Simple money tracking",
            description = "Record Money In and Money Out clearly."
        )

        FeatureCard(
            icon = Icons.Default.AccountBalance,
            title = "Loans and installments",
            description = "Track principal, payments and remaining debt."
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        FinancialPreview()

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Button(
            onClick = onSignup,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Create free account",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Log in",
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
    icon: ImageVector,
    title: String,
    description: String
) {
    Surface(
        color = Color.White.copy(alpha = 0.06f),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFF18C6B5).copy(alpha = 0.14f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF4FE6D8),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(25.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun FinancialPreview() {
    Surface(
        color = Color.White.copy(alpha = 0.06f),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "FINANCIAL PREVIEW",
                color = Color(0xFF4FE6D8),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Everything important in one view",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF108D83),
                                Color(0xFF135B82)
                            )
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "Remaining balance",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 12.sp
                    )

                    Text(
                        text = "€2,070.00",
                        color = Color.White,
                        fontSize = 31.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Income minus expenses",
                        color = Color(0xFF6FF4E7),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PreviewMetric(
                    title = "Income",
                    value = "+ €4,250",
                    color = Color(0xFF25CF69),
                    modifier = Modifier.weight(1f)
                )

                PreviewMetric(
                    title = "Expenses",
                    value = "− €2,180",
                    color = Color(0xFFFF4D59),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PreviewMetric(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier
) {
    Surface(
        color = Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.60f),
                fontSize = 12.sp
            )

            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun AuthenticationScreen(
    signup: Boolean,
    onBack: () -> Unit,
    onLoginSuccess: (UserSession) -> Unit,
    onSignupComplete: () -> Unit
) {
    val authentication = remember {
        SupabaseAuth()
    }

    val scope = rememberCoroutineScope()

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (signup) {
                            "Create account"
                        } else {
                            "Log in"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(22.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(70.dp)
            )

            Text(
                text = "Nomad Wealth",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (signup) {
                    "Start building financial clarity"
                } else {
                    "Welcome back"
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            if (signup) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Your name")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text("Email")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (signup) {
                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    label = {
                        Text("Confirm password")
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (message.isNotBlank()) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = message,
                    color = if (message.startsWith("Account created")) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = {
                    message = ""

                    if (!email.contains("@")) {
                        message = "Enter a valid email address."
                        return@Button
                    }

                    if (password.length < 6) {
                        message = "Password must have at least 6 characters."
                        return@Button
                    }

                    if (signup && name.isBlank()) {
                        message = "Enter your name."
                        return@Button
                    }

                    if (signup && password != confirmPassword) {
                        message = "Passwords do not match."
                        return@Button
                    }

                    loading = true

                    scope.launch {
                        if (signup) {
                            val result = withContext(Dispatchers.IO) {
                                authentication.signUp(
                                    name = name.trim(),
                                    email = email.trim(),
                                    password = password
                                )
                            }

                            loading = false

                            result
                                .onSuccess { resultMessage ->
                                    message = resultMessage
                                    onSignupComplete()
                                }
                                .onFailure { error ->
                                    message = error.message
                                        ?: "Registration failed."
                                }
                        } else {
                            val result = withContext(Dispatchers.IO) {
                                authentication.signIn(
                                    email = email.trim(),
                                    password = password
                                )
                            }

                            loading = false

                            result
                                .onSuccess { userSession ->
                                    onLoginSuccess(userSession)
                                }
                                .onFailure { error ->
                                    message = error.message
                                        ?: "Login failed."
                                }
                        }
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (signup) {
                            "Create account"
                        } else {
                            "Log in"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!authentication.configured()) {
                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "Authentication needs GitHub Secrets.\nThe demonstration interface can still be built.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MainApplication(
    session: UserSession,
    onLogout: () -> Unit
) {
    var selectedTab by remember {
        mutableStateOf(AppTab.DASHBOARD)
    }

    val accounts = remember {
        mutableStateListOf(
            Account(
                id = 1,
                name = "Main account",
                currency = "EUR",
                balance = 1450.0
            ),
            Account(
                id = 2,
                name = "Freelance wallet",
                currency = "USD",
                balance = 620.0
            )
        )
    }

    val transactions = remember {
        mutableStateListOf(
            Transaction(
                id = 1,
                title = "Freelance payment",
                category = "Income",
                amount = 600.0,
                income = true,
                date = "Today"
            ),
            Transaction(
                id = 2,
                title = "Rent",
                category = "Housing",
                amount = 400.0,
                income = false,
                date = "Today"
            ),
            Transaction(
                id = 3,
                title = "Groceries",
                category = "Food",
                amount = 48.0,
                income = false,
                date = "Yesterday"
            )
        )
    }

    val loans = remember {
        mutableStateListOf(
            Loan(
                id = 1,
                name = "Education loan",
                principal = 5000.0,
                remaining = 3200.0,
                monthlyPayment = 180.0,
                currency = "EUR"
            )
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding()
            ) {
                AppTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
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
                AppTab.DASHBOARD -> {
                    DashboardScreen(
                        name = session.name,
                        accounts = accounts,
                        transactions = transactions
                    )
                }

                AppTab.ACCOUNTS -> {
                    AccountsScreen(
                        accounts = accounts
                    )
                }

                AppTab.TRANSACTIONS -> {
                    TransactionsScreen(
                        transactions = transactions
                    )
                }

                AppTab.LOANS -> {
                    LoansScreen(
                        loans = loans
                    )
                }

                AppTab.PROFILE -> {
                    ProfileScreen(
                        session = session,
                        accountCount = accounts.size,
                        transactionCount = transactions.size,
                        loanCount = loans.size,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    name: String,
    accounts: List<Account>,
    transactions: List<Transaction>
) {
    val totalIncome = transactions
        .filter { transaction ->
            transaction.income
        }
        .sumOf { transaction ->
            transaction.amount
        }

    val totalExpenses = transactions
        .filter { transaction ->
            !transaction.income
        }
        .sumOf { transaction ->
            transaction.amount
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 18.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Overview",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Surface(
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.12f
                ),
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ) {
                        Text(
                            text = name
                                .take(1)
                                .uppercase(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 12.dp
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column {
                        Text(
                            text = greetingText().uppercase(),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = name,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Here is your financial overview",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardMetric(
                    title = "Income",
                    value = totalIncome,
                    color = Color(0xFF25CF69),
                    modifier = Modifier.weight(1f)
                )

                DashboardMetric(
                    title = "Expenses",
                    value = totalExpenses,
                    color = Color(0xFFFF4D59),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            DashboardMetric(
                title = "Remaining this month",
                value = totalIncome - totalExpenses,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Financial accounts",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Total balance across ${accounts.size} accounts",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(accounts) { account ->
            AccountRow(
                account = account
            )
        }

        item {
            Text(
                text = "Recent transactions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(transactions.take(5)) { transaction ->
            TransactionRow(
                transaction = transaction
            )
        }
    }
}

private fun greetingText(): String {
    val hour = Calendar
        .getInstance()
        .get(Calendar.HOUR_OF_DAY)

    return when {
        hour < 12 -> "Good morning"
        hour < 18 -> "Good afternoon"
        else -> "Good evening"
    }
}

@Composable
private fun DashboardMetric(
    title: String,
    value: Double,
    color: Color,
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
                text = formatMoney(value),
                color = color,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AccountsScreen(
    accounts: MutableList<Account>
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            title = "Accounts",
            subtitle = "Manage money across currencies",
            action = {
                FilledTonalButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Text(" Add")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                bottom = 24.dp
            )
        ) {
            items(accounts) { account ->
                AccountRow(
                    account = account
                )
            }
        }
    }

    if (showDialog) {
        SimpleAddDialog(
            title = "Add account",
            fieldLabel = "Account name",
            onDismiss = {
                showDialog = false
            },
            onAdd = { accountName ->
                accounts.add(
                    Account(
                        id = System.currentTimeMillis(),
                        name = accountName,
                        currency = "EUR",
                        balance = 0.0
                    )
                )

                showDialog = false
            }
        )
    }
}

@Composable
private fun TransactionsScreen(
    transactions: MutableList<Transaction>
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            title = "Transactions",
            subtitle = "Your Money In and Money Out",
            action = {
                FilledTonalButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Text(" Add")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                bottom = 24.dp
            )
        ) {
            items(transactions) { transaction ->
                TransactionRow(
                    transaction = transaction
                )
            }
        }
    }

    if (showDialog) {
        SimpleAddDialog(
            title = "Add transaction",
            fieldLabel = "Transaction title",
            onDismiss = {
                showDialog = false
            },
            onAdd = { transactionTitle ->
                transactions.add(
                    0,
                    Transaction(
                        id = System.currentTimeMillis(),
                        title = transactionTitle,
                        category = "Other",
                        amount = 25.0,
                        income = false,
                        date = "Today"
                    )
                )

                showDialog = false
            }
        )
    }
}

@Composable
private fun LoansScreen(
    loans: MutableList<Loan>
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            title = "Loans",
            subtitle = "Track installments and remaining debt",
            action = {
                FilledTonalButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Text(" Add")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                bottom = 24.dp
            )
        ) {
            items(loans) { loan ->
                LoanRow(
                    loan = loan
                )
            }
        }
    }

    if (showDialog) {
        SimpleAddDialog(
            title = "Add loan",
            fieldLabel = "Loan name",
            onDismiss = {
                showDialog = false
            },
            onAdd = { loanName ->
                loans.add(
                    Loan(
                        id = System.currentTimeMillis(),
                        name = loanName,
                        principal = 1000.0,
                        remaining = 1000.0,
                        monthlyPayment = 100.0,
                        currency = "EUR"
                    )
                )

                showDialog = false
            }
        )
    }
}

@Composable
private fun LoanRow(
    loan: Loan
) {
    val paidAmount = loan.principal - loan.remaining

    val progress = if (loan.principal > 0) {
        (paidAmount / loan.principal)
            .toFloat()
            .coerceIn(
                minimumValue = 0f,
                maximumValue = 1f
            )
    } else {
        0f
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = loan.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = "Remaining: ${loan.currency} ${loan.remaining}",
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = "Monthly payment: ${loan.currency} ${loan.monthlyPayment}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    session: UserSession,
    accountCount: Int,
    transactionCount: Int,
    loanCount: Int,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Surface(
            color = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.12f
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Text(
                        text = session.name
                            .take(2)
                            .uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(22.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = session.name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = session.email.ifBlank {
                        "Demonstration profile"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileStat(
                value = accountCount.toString(),
                label = "Accounts",
                modifier = Modifier.weight(1f)
            )

            ProfileStat(
                value = transactionCount.toString(),
                label = "Entries",
                modifier = Modifier.weight(1f)
            )

            ProfileStat(
                value = loanCount.toString(),
                label = "Loans",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Account security",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Supabase email authentication",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "HTTPS-only network communication",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "No service-role key is stored in the app",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text("Log out")
        }
    }
}

@Composable
private fun ScreenHeader(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        action()
    }
}

@Composable
private fun AccountRow(
    account: Account
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = account.name,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = account.currency,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${account.currency} ${
                    String.format(
                        Locale.US,
                        "%.2f",
                        account.balance
                    )
                }",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction
) {
    val transactionColor = if (transaction.income) {
        Color(0xFF25CF69)
    } else {
        Color(0xFFFF4D59)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (transaction.income) {
                    Icons.Default.ArrowCircleDown
                } else {
                    Icons.Default.ArrowCircleUp
                },
                contentDescription = null,
                tint = transactionColor
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${transaction.category} · ${transaction.date}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${if (transaction.income) "+" else "-"}${
                    formatMoney(transaction.amount)
                }",
                color = transactionColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 15.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SimpleAddDialog(
    title: String,
    fieldLabel: String,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = it
                },
                label = {
                    Text(fieldLabel)
                },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (value.isNotBlank()) {
                        onAdd(value.trim())
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun formatMoney(
    value: Double
): String {
    return NumberFormat
        .getCurrencyInstance(Locale.GERMANY)
        .format(value)
}
