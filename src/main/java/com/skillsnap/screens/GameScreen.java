package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.database.PlayerDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.game.GameResult;          // ← this one
import com.skillsnap.models.game.MiniGame;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.engine.BadgeEngine;
import java.util.ArrayList;

import com.skillsnap.utils.AnimationUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameScreen {

    private MiniGame   game;
    private CareerPath career;
    private GameDAO    gameDAO   = new GameDAO();
    private PlayerDAO  playerDAO = new PlayerDAO();

    private int     score       = 0;
    private int     timeLeft;
    private int     currentQ    = 0;
    private int     totalQ      = 5;
    private Timeline timer;
    private long    startTime;

    // UI refs updated during game
    private Text    scoreText;
    private Text    timerText;
    private Text    progressText;
    private VBox    questionArea;

    // Questions for this session
    private List<Question> questions = new ArrayList<>();

    public GameScreen(MiniGame game, CareerPath career) {
        this.game    = game;
        this.career  = career;
        this.timeLeft = game.getTimeLimitSec();
    }

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── TOP BAR ───────────────────────────────────────────
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 30, 16, 30));
        topBar.setSpacing(20);
        topBar.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-width: 0 0 1 0;");

        // Career + game name
        VBox gameInfo = new VBox(2);
        Text careerTag = new Text(career.getTitle());
        careerTag.setStyle(
                "-fx-font-size: 12px; -fx-fill: #2E75B6;");
        Text gameTitle = new Text(game.getTitle());
        gameTitle.setStyle("-fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-fill: white;");
        gameInfo.getChildren().addAll(careerTag, gameTitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Progress
        progressText = new Text("Question 1 / " + totalQ);
        progressText.setStyle(
                "-fx-font-size: 14px; -fx-fill: #8B949E;");

        // Score
        scoreText = new Text("Score: 0");
        scoreText.setStyle("-fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-fill: #2E75B6;");

        // Timer
        timerText = new Text(timeLeft + "s");
        timerText.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-fill: #3FB950;");

        topBar.getChildren().addAll(
                gameInfo, spacer,
                progressText, scoreText, timerText);
        root.setTop(topBar);

        // ── QUESTION AREA ─────────────────────────────────────
        questionArea = new VBox(24);
        questionArea.setAlignment(Pos.CENTER);
        questionArea.setPadding(new Insets(40));

        // Instructions card shown first
        showInstructions();

        root.setCenter(questionArea);

        // Start timer
        startTimer();
        startTime = System.currentTimeMillis();

        return root;
    }

    // ── INSTRUCTIONS ──────────────────────────────────────────
    private void showInstructions() {
        questionArea.getChildren().clear();

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");
        card.setMaxWidth(600);

        Text instrTitle = new Text("How to Play");
        instrTitle.setStyle("-fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text instrText = new Text(game.getInstructions());
        instrText.setStyle(
                "-fx-font-size: 15px; -fx-fill: #C9D1D9;");
        instrText.setWrappingWidth(520);

        Text diffText = new Text("Difficulty: " +
                game.getDifficulty() + "  •  " +
                game.getTimeLimitSec() + " seconds  •  " +
                game.getXpReward() + " XP reward");
        diffText.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");

        Button startBtn = new Button("Start Game");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setPrefWidth(200);
        startBtn.setPrefHeight(46);
        startBtn.setOnAction(e -> {
            generateQuestions();
            showQuestion();
        });

        card.getChildren().addAll(
                instrTitle, instrText, diffText, startBtn);
        questionArea.getChildren().add(card);
    }

    // ── GENERATE QUESTIONS ────────────────────────────────────
    private void generateQuestions() {
        questions.clear();

        switch (game.getGameType()) {

            // ── DATA SCIENCE ──────────────────────────────────────
            case "pattern" -> {
                questions.add(new Question(
                        "What comes next?  2,  4,  8,  16,  __",
                        new String[]{"24","32","20","28"}, "32",
                        "Pattern: multiply by 2 each time."));
                questions.add(new Question(
                        "What comes next?  1,  4,  9,  16,  __",
                        new String[]{"20","25","30","22"}, "25",
                        "Pattern: perfect squares 1²,2²,3²,4²,5²."));
                questions.add(new Question(
                        "What comes next?  3,  6,  12,  24,  __",
                        new String[]{"36","30","48","42"}, "48",
                        "Pattern: multiply by 2 each time."));
                questions.add(new Question(
                        "What comes next?  1,1,2,3,5,8,  __",
                        new String[]{"11","12","13","15"}, "13",
                        "Fibonacci: each = sum of previous two."));
                questions.add(new Question(
                        "What comes next?  100,50,25,12.5,  __",
                        new String[]{"6","6.25","7","5"}, "6.25",
                        "Pattern: divide by 2 each time."));
            }

            case "chart_reading" -> {
                questions.add(new Question(
                        "A bar chart shows sales: Jan=100, Feb=150, Mar=120. Which month had highest sales?",
                        new String[]{"January","February","March","All equal"}, "February",
                        "February had the highest bar at 150."));
                questions.add(new Question(
                        "A pie chart shows: 50% Tech, 30% Finance, 20% Health. What sector is smallest?",
                        new String[]{"Tech","Finance","Health","Equal"}, "Health",
                        "Health has the smallest slice at 20%."));
                questions.add(new Question(
                        "Line graph shows temperature: Mon=20°, Tue=22°, Wed=19°, Thu=25°. Biggest jump between?",
                        new String[]{"Mon-Tue","Tue-Wed","Wed-Thu","Thu-Fri"}, "Wed-Thu",
                        "Wed to Thu increased by 6 degrees — the biggest jump."));
                questions.add(new Question(
                        "Dataset: [2,4,4,4,5,5,7,9]. What is the mean?",
                        new String[]{"4","5","4.5","5.5"}, "5",
                        "Mean = sum/count = 40/8 = 5."));
                questions.add(new Question(
                        "Which chart type is best for showing trend over time?",
                        new String[]{"Pie chart","Bar chart","Line chart","Scatter plot"}, "Line chart",
                        "Line charts are designed for showing change over time."));
            }

            case "outlier" -> {
                questions.add(new Question(
                        "Which number does NOT belong?  2, 4, 7, 8, 16",
                        new String[]{"2","4","7","16"}, "7",
                        "All others are even numbers. 7 is odd."));
                questions.add(new Question(
                        "Which does NOT belong?  10, 20, 30, 45, 50",
                        new String[]{"10","20","45","50"}, "45",
                        "All others are multiples of 10. 45 is not."));
                questions.add(new Question(
                        "Which does NOT belong?  Apple, Banana, Carrot, Mango",
                        new String[]{"Apple","Banana","Carrot","Mango"}, "Carrot",
                        "All others are fruits. Carrot is a vegetable."));
                questions.add(new Question(
                        "Which number does NOT belong?  1, 4, 9, 15, 25",
                        new String[]{"1","4","15","25"}, "15",
                        "All others are perfect squares. 15 is not."));
                questions.add(new Question(
                        "Dataset: [5,5,5,5,50,5,5]. Which is the outlier?",
                        new String[]{"First 5","50","Last 5","None"}, "50",
                        "50 is far from the cluster of 5s — classic outlier."));
            }

            // ── SOFTWARE ENGINEERING ──────────────────────────────
            case "logic_puzzle" -> {
                questions.add(new Question(
                        "To make a sandwich, what is the correct order?",
                        new String[]{
                                "Fill → Bread → Bread",
                                "Bread → Fill → Bread",
                                "Bread → Bread → Fill",
                                "Fill → Fill → Bread"},
                        "Bread → Fill → Bread",
                        "Bottom bread first, then filling, then top bread."));
                questions.add(new Question(
                        "To log into a website, correct order is?",
                        new String[]{
                                "Submit → Password → Username",
                                "Username → Submit → Password",
                                "Username → Password → Submit",
                                "Password → Username → Submit"},
                        "Username → Password → Submit",
                        "Enter credentials first, then submit."));
                questions.add(new Question(
                        "To print a document, correct order is?",
                        new String[]{
                                "Print → Open → Select",
                                "Open → Print → Select",
                                "Select → Open → Print",
                                "Open → Select → Print"},
                        "Open → Select → Print",
                        "Open the file, select printer settings, then print."));
                questions.add(new Question(
                        "In a for loop: what executes first?",
                        new String[]{
                                "Loop body","Condition check",
                                "Initialization","Increment"},
                        "Initialization",
                        "for(init; condition; increment) — init runs once first."));
                questions.add(new Question(
                        "Which is the correct way to declare a variable in Java?",
                        new String[]{
                                "x int = 5;",
                                "int x = 5;",
                                "5 = int x;",
                                "variable x = 5;"},
                        "int x = 5;",
                        "Java syntax: type, then name, then value."));
            }

            case "algorithm" -> {
                questions.add(new Question(
                        "What is the Big O of searching an unsorted array?",
                        new String[]{"O(1)","O(log n)","O(n)","O(n²)"}, "O(n)",
                        "You may need to check every element — linear time O(n)."));
                questions.add(new Question(
                        "What is the Big O of Binary Search?",
                        new String[]{"O(1)","O(log n)","O(n)","O(n²)"}, "O(log n)",
                        "Binary search halves the search space each step — O(log n)."));
                questions.add(new Question(
                        "Which sorting algorithm has worst case O(n²)?",
                        new String[]{"Merge Sort","Quick Sort","Bubble Sort","Heap Sort"}, "Bubble Sort",
                        "Bubble Sort compares every pair — O(n²) worst case."));
                questions.add(new Question(
                        "What data structure uses LIFO order?",
                        new String[]{"Queue","Stack","Array","LinkedList"}, "Stack",
                        "Stack = Last In First Out. Like a stack of plates."));
                questions.add(new Question(
                        "What is the time complexity of accessing array element by index?",
                        new String[]{"O(n)","O(log n)","O(1)","O(n²)"}, "O(1)",
                        "Array index access is always constant time O(1)."));
            }

            case "bugfinder" -> {
                questions.add(new Question(
                        "int x = 5; if(x = 5) { print(x); } — What is the bug?",
                        new String[]{
                                "Wrong variable name",
                                "= should be ==",
                                "Missing semicolon",
                                "Wrong data type"},
                        "= should be ==",
                        "= is assignment, == is comparison. Classic Java bug."));
                questions.add(new Question(
                        "for(int i=0; i<=arr.length; i++) — What is the bug?",
                        new String[]{
                                "Wrong loop variable",
                                "i<=arr.length causes ArrayIndexOutOfBounds",
                                "Missing increment",
                                "No bug"},
                        "i<=arr.length causes ArrayIndexOutOfBounds",
                        "Last valid index is arr.length-1. Should be i<arr.length."));
                questions.add(new Question(
                        "String s = null; if(s.equals(\"hello\")) — What is the bug?",
                        new String[]{
                                "Wrong string value",
                                "NullPointerException — s is null",
                                "Missing semicolon",
                                "Wrong method name"},
                        "NullPointerException — s is null",
                        "Calling .equals() on null throws NullPointerException."));
                questions.add(new Question(
                        "int result = 7/2; — What unexpected result occurs?",
                        new String[]{
                                "3.5","3","4","Error"},
                        "3",
                        "int/int = int in Java. Decimal is truncated. Use double."));
                questions.add(new Question(
                        "Which line causes infinite loop?\nwhile(true) { int i=0; i++; }",
                        new String[]{
                                "while(true)","int i=0","i++","No infinite loop"},
                        "while(true)",
                        "i is reset to 0 every iteration. Loop never ends."));
            }

            // ── CYBER SECURITY ────────────────────────────────────
            case "cipher" -> {
                questions.add(new Question(
                        "Caesar cipher shift 3: 'KHOOR' decodes to?",
                        new String[]{"HELLO","WORLD","JAPAN","HOUSE"}, "HELLO",
                        "Shift each letter back 3: K→H, H→E, O→L, O→L, R→O."));
                questions.add(new Question(
                        "What does hashing do to a password?",
                        new String[]{
                                "Encrypts it so it can be decrypted",
                                "Converts to fixed-length irreversible string",
                                "Stores it in plain text",
                                "Compresses it"},
                        "Converts to fixed-length irreversible string",
                        "Hashing is one-way — you cannot reverse a hash."));
                questions.add(new Question(
                        "Which is the strongest password?",
                        new String[]{
                                "password123",
                                "MyName2000",
                                "Tr0ub4dor&3",
                                "123456"},
                        "Tr0ub4dor&3",
                        "Mix of upper, lower, numbers, symbols = strong password."));
                questions.add(new Question(
                        "What does HTTPS stand for?",
                        new String[]{
                                "HyperText Transfer Protocol Secure",
                                "High Transfer Protocol System",
                                "HyperText Transport Program Secure",
                                "None of these"},
                        "HyperText Transfer Protocol Secure",
                        "HTTPS = HTTP with TLS/SSL encryption layer."));
                questions.add(new Question(
                        "Which attack tricks users into revealing passwords?",
                        new String[]{"DDoS","SQL Injection","Phishing","Ransomware"},
                        "Phishing",
                        "Phishing uses fake websites/emails to steal credentials."));
            }

            case "log_analysis" -> {
                questions.add(new Question(
                        "Log shows 1000 login attempts from one IP in 1 minute. This is?",
                        new String[]{
                                "Normal activity",
                                "Brute force attack",
                                "DDoS attack",
                                "SQL injection"},
                        "Brute force attack",
                        "Rapid repeated login attempts = brute force password cracking."));
                questions.add(new Question(
                        "Log: GET /admin?id=1 OR 1=1-- What attack is this?",
                        new String[]{
                                "XSS","SQL Injection","CSRF","Phishing"},
                        "SQL Injection",
                        "OR 1=1-- is a classic SQL injection payload."));
                questions.add(new Question(
                        "Error 403 in logs means?",
                        new String[]{
                                "Page not found",
                                "Server error",
                                "Access forbidden",
                                "Redirect"},
                        "Access forbidden",
                        "HTTP 403 = Forbidden. User doesn't have permission."));
                questions.add(new Question(
                        "Which HTTP status code means successful request?",
                        new String[]{"404","500","200","301"}, "200",
                        "200 OK = request succeeded."));
                questions.add(new Question(
                        "Log shows response time 30000ms for all requests suddenly. Likely cause?",
                        new String[]{
                                "Normal load","DDoS attack",
                                "SQL injection","Phishing"},
                        "DDoS attack",
                        "Sudden massive slowdown often indicates Distributed Denial of Service."));
            }

            // ── AI / ML ───────────────────────────────────────────
            case "classification" -> {
                questions.add(new Question(
                        "Email with 'FREE MONEY CLICK NOW' — how would you classify it?",
                        new String[]{"Spam","Not spam","Important","Unknown"}, "Spam",
                        "Urgency + money + caps = classic spam indicators."));
                questions.add(new Question(
                        "Image shows a dog with four legs and fur. Classification?",
                        new String[]{"Cat","Dog","Bird","Fish"}, "Dog",
                        "Four legs + fur + correct shape = Dog classification."));
                questions.add(new Question(
                        "Customer spent $500 on electronics, $400 on gadgets. Segment?",
                        new String[]{
                                "Budget shopper",
                                "Tech enthusiast",
                                "Fashion buyer",
                                "Food lover"},
                        "Tech enthusiast",
                        "High spending on electronics/gadgets = Tech enthusiast segment."));
                questions.add(new Question(
                        "Tumor scan: irregular border, rapid growth, uneven color. Classification?",
                        new String[]{"Benign","Malignant","Unknown","Normal"}, "Malignant",
                        "Irregular borders + rapid growth = malignant indicators."));
                questions.add(new Question(
                        "Review: 'Absolutely terrible, never buying again'. Sentiment?",
                        new String[]{"Positive","Neutral","Negative","Mixed"}, "Negative",
                        "Strong negative words = Negative sentiment clearly."));
            }

            case "algorithm_select" -> {
                questions.add(new Question(
                        "Predicting house price from size, location, rooms. Best algorithm?",
                        new String[]{
                                "K-Means Clustering",
                                "Linear Regression",
                                "Decision Tree Classification",
                                "Neural Network"},
                        "Linear Regression",
                        "Predicting a continuous value (price) = Regression problem."));
                questions.add(new Question(
                        "Grouping customers with no predefined categories. Best algorithm?",
                        new String[]{
                                "Linear Regression",
                                "K-Means Clustering",
                                "Logistic Regression",
                                "SVM"},
                        "K-Means Clustering",
                        "No labels + grouping = Unsupervised clustering."));
                questions.add(new Question(
                        "Email spam detection (spam or not spam). Best algorithm?",
                        new String[]{
                                "K-Means","Linear Regression",
                                "Logistic Regression","PCA"},
                        "Logistic Regression",
                        "Binary classification (2 outcomes) = Logistic Regression."));
                questions.add(new Question(
                        "Image recognition for 1000 categories. Best approach?",
                        new String[]{
                                "Linear Regression",
                                "K-Means",
                                "Deep Neural Network (CNN)",
                                "Decision Tree"},
                        "Deep Neural Network (CNN)",
                        "Complex image tasks need Convolutional Neural Networks."));
                questions.add(new Question(
                        "Reducing 100 features to 10 key features. Technique?",
                        new String[]{
                                "K-Means","Linear Regression",
                                "PCA","Random Forest"},
                        "PCA",
                        "PCA = Principal Component Analysis — dimensionality reduction."));
            }

            // ── WEB DEVELOPMENT ──────────────────────────────────
            case "design_match" -> {
                questions.add(new Question(
                        "Which color combination has best contrast for readability?",
                        new String[]{
                                "White text on yellow",
                                "Black text on white",
                                "Grey text on light grey",
                                "Red text on green"},
                        "Black text on white",
                        "High contrast = best readability. Black on white is maximum contrast."));
                questions.add(new Question(
                        "Primary button should be which color vs secondary button?",
                        new String[]{
                                "Same color, different size",
                                "Filled strong color vs outlined/muted",
                                "Both same style",
                                "Secondary brighter than primary"},
                        "Filled strong color vs outlined/muted",
                        "Primary = filled, prominent. Secondary = outlined, less emphasis."));
                questions.add(new Question(
                        "Font size for body text on web is typically?",
                        new String[]{"8-10px","12-14px","16-18px","24px+"}, "16-18px",
                        "16px is the browser default. 16-18px is optimal for body text."));
                questions.add(new Question(
                        "Which layout principle puts most important content top-left?",
                        new String[]{
                                "Z-Pattern","F-Pattern",
                                "Center focus","Grid layout"},
                        "F-Pattern",
                        "Eye tracking shows users read in F-shape — top then down-left."));
                questions.add(new Question(
                        "Consistent spacing between elements is called?",
                        new String[]{
                                "Kerning","Leading",
                                "White space / Padding","Tracking"},
                        "White space / Padding",
                        "White space and padding create breathing room and visual hierarchy."));
            }

            case "responsive" -> {
                questions.add(new Question(
                        "Which CSS unit scales with parent element width?",
                        new String[]{"px","%","rem","vh"}, "%",
                        "% is relative to parent. Ideal for responsive widths."));
                questions.add(new Question(
                        "Mobile-first means?",
                        new String[]{
                                "Build desktop first then shrink",
                                "Only build for mobile",
                                "Design for mobile first then scale up",
                                "Use mobile framework"},
                        "Design for mobile first then scale up",
                        "Mobile-first = start with smallest screen, add complexity for larger."));
                questions.add(new Question(
                        "Breakpoint at 768px typically targets which device?",
                        new String[]{"Phone","Tablet","Desktop","TV"}, "Tablet",
                        "768px is the standard tablet breakpoint in most frameworks."));
                questions.add(new Question(
                        "Which meta tag is required for responsive design?",
                        new String[]{
                                "<meta charset='UTF-8'>",
                                "<meta name='viewport' content='width=device-width'>",
                                "<meta name='description'>",
                                "<meta http-equiv='refresh'>"},
                        "<meta name='viewport' content='width=device-width'>",
                        "Viewport meta tag tells browser to match device screen width."));
                questions.add(new Question(
                        "CSS Flexbox vs Grid: which is better for 1D layouts?",
                        new String[]{
                                "Grid","Flexbox",
                                "Both equal","Neither"},
                        "Flexbox",
                        "Flexbox = 1D (row OR column). Grid = 2D (rows AND columns)."));
            }

            // ── MOBILE DEVELOPMENT ────────────────────────────────
            case "ux_flow" -> {
                questions.add(new Question(
                        "User wants to post a photo on Instagram. Correct flow?",
                        new String[]{
                                "Post → Select → Camera",
                                "Camera → Select → Post",
                                "Select → Camera → Post",
                                "Camera → Post → Select"},
                        "Camera → Select → Post",
                        "Open camera, take/select photo, then post it."));
                questions.add(new Question(
                        "Onboarding flow for new app users should be?",
                        new String[]{
                                "Login → Features → Welcome",
                                "Features → Login → Welcome",
                                "Welcome → Features → Signup",
                                "Signup → Welcome → Features"},
                        "Welcome → Features → Signup",
                        "Show value first (welcome + features), then ask for signup."));
                questions.add(new Question(
                        "E-commerce checkout flow correct order?",
                        new String[]{
                                "Payment → Cart → Address",
                                "Cart → Payment → Address",
                                "Cart → Address → Payment",
                                "Address → Cart → Payment"},
                        "Cart → Address → Payment",
                        "Review cart, enter delivery address, then payment last."));
                questions.add(new Question(
                        "Which screen should always have a back button?",
                        new String[]{
                                "Home screen only",
                                "Every screen except home",
                                "Settings only",
                                "No screens"},
                        "Every screen except home",
                        "Every non-root screen needs back navigation for UX."));
                questions.add(new Question(
                        "Password reset flow correct order?",
                        new String[]{
                                "New password → Email → OTP",
                                "Email → OTP → New password",
                                "OTP → Email → New password",
                                "New password → OTP → Email"},
                        "Email → OTP → New password",
                        "Enter email, verify with OTP, then set new password."));
            }

            case "gesture_match" -> {
                questions.add(new Question(
                        "Which gesture dismisses a notification on mobile?",
                        new String[]{"Tap","Double tap","Swipe right","Long press"},
                        "Swipe right",
                        "Swipe right/left to dismiss is the standard notification gesture."));
                questions.add(new Question(
                        "Which gesture zooms into a map?",
                        new String[]{
                                "Single tap",
                                "Pinch outward (spread)",
                                "Pinch inward",
                                "Double swipe"},
                        "Pinch outward (spread)",
                        "Spread two fingers apart = zoom in. Pinch together = zoom out."));
                questions.add(new Question(
                        "Which gesture selects multiple items in a list?",
                        new String[]{"Tap","Swipe","Long press then tap","Double tap"},
                        "Long press then tap",
                        "Long press activates multi-select mode, then tap to select more."));
                questions.add(new Question(
                        "Pull-to-refresh is triggered by?",
                        new String[]{
                                "Swipe left","Swipe up from bottom",
                                "Pull down from top","Double tap"},
                        "Pull down from top",
                        "Standard pull-to-refresh: drag down from the top of a list."));
                questions.add(new Question(
                        "Which gesture goes back on Android?",
                        new String[]{
                                "Swipe up","Swipe right from left edge",
                                "Double tap","Pinch"},
                        "Swipe right from left edge",
                        "Android back gesture = swipe inward from left or right edge."));
            }

            // ── DEFAULT fallback ──────────────────────────────────
            default -> {
                questions.add(new Question(
                        "What does OOP stand for?",
                        new String[]{
                                "Object Oriented Programming",
                                "Open Output Processing",
                                "Ordered Object Protocol",
                                "None"},
                        "Object Oriented Programming",
                        "OOP = Object Oriented Programming. Encapsulation, Inheritance, Polymorphism."));
                questions.add(new Question(
                        "Which keyword creates an object in Java?",
                        new String[]{"create","build","new","make"}, "new",
                        "new ClassName() creates an object in Java."));
                questions.add(new Question(
                        "What is the entry point of a Java program?",
                        new String[]{
                                "start()","run()",
                                "main()","init()"},
                        "main()",
                        "public static void main(String[] args) is always the entry point."));
                questions.add(new Question(
                        "What does private mean in Java?",
                        new String[]{
                                "Accessible everywhere",
                                "Accessible only in same class",
                                "Accessible in subclasses",
                                "Accessible in same package"},
                        "Accessible only in same class",
                        "private = most restrictive. Only the class that owns it can access."));
                questions.add(new Question(
                        "Which collection allows duplicate elements?",
                        new String[]{"Set","HashMap","ArrayList","HashSet"},
                        "ArrayList",
                        "ArrayList allows duplicates. Set does not allow duplicates."));
            }
        }

        Collections.shuffle(questions);
    }

    // ── SHOW CURRENT QUESTION ─────────────────────────────────
    private void showQuestion() {
        if (currentQ >= totalQ) {
            endGame();
            return;
        }

        questionArea.getChildren().clear();
        progressText.setText(
                "Question " + (currentQ + 1) + " / " + totalQ);

        Question q = questions.get(currentQ);

        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");
        card.setMaxWidth(640);

        Text qText = new Text(q.questionText);
        qText.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-fill: white;");
        qText.setWrappingWidth(580);

        // Answer buttons in 2x2 grid
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setAlignment(Pos.CENTER);

        String[] options = q.options;
        for (int i = 0; i < options.length; i++) {
            Button optBtn = new Button(options[i]);
            optBtn.setPrefWidth(240);
            optBtn.setPrefHeight(54);
            optBtn.setStyle(
                    "-fx-background-color: #161B22;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #30363D;" +
                            "-fx-border-radius: 8;" +
                            "-fx-cursor: hand;");

            String answer = options[i];
            optBtn.setOnAction(e ->
                    handleAnswer(answer, q, card));

            // Hover effect
            optBtn.setOnMouseEntered(e ->
                    optBtn.setStyle(
                            "-fx-background-color: #1C2128;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 16px;" +
                                    "-fx-background-radius: 8;" +
                                    "-fx-border-color: #2E75B6;" +
                                    "-fx-border-radius: 8;" +
                                    "-fx-cursor: hand;"));
            optBtn.setOnMouseExited(e ->
                    optBtn.setStyle(
                            "-fx-background-color: #161B22;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 16px;" +
                                    "-fx-background-radius: 8;" +
                                    "-fx-border-color: #30363D;" +
                                    "-fx-border-radius: 8;" +
                                    "-fx-cursor: hand;"));

            grid.add(optBtn, i % 2, i / 2);
        }

        card.getChildren().addAll(qText, grid);
        questionArea.getChildren().add(card);
    }

    // ── HANDLE ANSWER ─────────────────────────────────────────
    private void handleAnswer(String chosen,
                              Question q, VBox card) {
        boolean correct = chosen.equals(q.correctAnswer);

        // Points: correct = 20, speed bonus if under half time
        if (correct) {
            score += 20;
            scoreText.setText("Score: " + score);
        }

        // Show feedback briefly then next question
        Text feedback = new Text(correct ?
                "Correct! " + q.explanation :
                "Wrong. " + q.explanation);
        feedback.setStyle("-fx-font-size: 14px; " +
                "-fx-fill: " + (correct ? "#3FB950" : "#F85149") + ";");
        feedback.setWrappingWidth(560);
        card.getChildren().add(feedback);

        // Move to next question after 1.5 seconds
        Timeline pause = new Timeline(new KeyFrame(
                Duration.millis(1500), e -> {
            currentQ++;
            showQuestion();
        }));
        pause.play();
        if (!correct) {
            AnimationUtils.shake(card);
        } else {
            AnimationUtils.bounce(card);
        }
// import: import com.skillsnap.utils.AnimationUtils;
    }

    // ── TIMER ─────────────────────────────────────────────────
    private void startTimer() {
        timer = new Timeline(new KeyFrame(
                Duration.seconds(1), e -> {
            timeLeft--;
            timerText.setText(timeLeft + "s");

            // Color changes as time runs low
            if (timeLeft <= 10) {
                timerText.setStyle(
                        "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-fill: #F85149;");
            } else if (timeLeft <= 20) {
                timerText.setStyle(
                        "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-fill: #E3B341;");
            }

            if (timeLeft <= 0) endGame();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void endGame() {
        if (timer != null) timer.stop();

        int timeTaken = game.getTimeLimitSec() - timeLeft;
        Player player =
                PlayerSession.getInstance().getCurrentPlayer();

        // Save session to DB
        gameDAO.saveSession(
                player.getPlayerId(),
                game.getGameId(),
                score,
                game.getMaxScore(),
                timeTaken
        );

        // Add XP
        int xpEarned = score >= 90 ? 100 :
                score >= 70 ? 75  :
                        score >= 50 ? 50  : 25;
        playerDAO.addXP(player.getPlayerId(), xpEarned);

        // Update streak
        playerDAO.updateStreak(player.getPlayerId());

        // Check badges
        BadgeEngine badgeEngine = new BadgeEngine();
        ArrayList<String> newBadges =
                badgeEngine.checkAndAward(player, score,
                        game.getMaxScore());

        // Refresh player in session with updated XP
        Player updated =
                playerDAO.getPlayerById(player.getPlayerId());
        if (updated != null)
            PlayerSession.getInstance().login(updated);

        // Build result
        double pct = (score * 100.0) / game.getMaxScore();
        String message = pct >= 90 ? "Outstanding!" :
                pct >= 70 ? "Great work!"  :
                        pct >= 50 ? "Good effort!" :
                                "Keep practicing!";

        // Append badge info to message if earned
        if (!newBadges.isEmpty()) {
            message += "\nNew badge: " + newBadges.get(0) + "!";
        }

        GameResult result = new GameResult(
                game.getGameId(), score, game.getMaxScore(),
                timeTaken, xpEarned, message);

        ScreenManager.getInstance().showResults(result, career);
    }

    // ── Inner class — Question ────────────────────────────────
    private static class Question {
        String   questionText;
        String[] options;
        String   correctAnswer;
        String   explanation;

        Question(String q, String[] opts,
                 String correct, String explain) {
            this.questionText  = q;
            this.options       = opts;
            this.correctAnswer = correct;
            this.explanation   = explain;
        }
    }
}