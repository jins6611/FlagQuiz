package com.example.myapplication.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.presentation.viewModel.StartChallengeViewModel
import com.example.myapplication.model.MainQuestion
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import kotlinx.coroutines.delay

class StartChallengeActivity : ComponentActivity() {

    private lateinit var flagImage: MutableList<Int>
    private val viewmodel: StartChallengeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.White
                ) {
                    viewmodel.getQuestions(this)
                    flagImage = mutableListOf()
                    addImages(flagImage)
                    val questionList by viewmodel.questionList.collectAsState(null)
                    if (questionList != null) {
                        InitiateUI(questionList, flagImage)
                    }
                }
            }
        }
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY")
@Composable
fun InitiateUI(questionList: MainQuestion?, flagImage: MutableList<Int>) {
    var timeLeft by remember { mutableStateOf(30) }
    var valuationTimeLeft by remember { mutableStateOf(10) }
    val numberOfCorrectAnswers = remember { mutableIntStateOf(0) }

    val waitForAnswer = remember { mutableStateOf(false) }
    val startChallengeUI = remember { mutableStateOf(true) }
    val nextQuestion = remember { mutableStateOf(false) }
    val gameOverUI = remember { mutableStateOf(false) }
    val scoreUI = remember { mutableStateOf(false) }


    val validatedAnswer = remember { mutableStateOf(false) }
    val questionNumber = remember { mutableIntStateOf(0) }
    val reRunTimer = remember { mutableStateOf(false) }
    val qno = remember { mutableIntStateOf(1) }

    var userSelected by remember { mutableStateOf(false) }
    var userSelected1 by remember { mutableStateOf(false) }
    var userSelected2 by remember { mutableStateOf(false) }
    var userSelected3 by remember { mutableStateOf(false) }

    var noUserSelected by remember { mutableStateOf(false) }
    var noUserSelected1 by remember { mutableStateOf(false) }
    var noUserSelected2 by remember { mutableStateOf(false) }
    var noUserSelected3 by remember { mutableStateOf(false) }

    var answerState by remember { mutableIntStateOf(1) }
    var answerState1 by remember { mutableIntStateOf(1) }
    var answerState2 by remember { mutableIntStateOf(1) }
    var answerState3 by remember { mutableIntStateOf(1) }

    val color = if (!noUserSelected) {
        if (userSelected) {
            Color(0xFFDDDD50)
        } else {
            Color.White
        }
    } else {
        when (answerState) {
            1 -> {
                Color.White
            }

            2 -> {
                Color(0xFF2A8B2E)
            }

            3 -> {
                Color(0xFFF44336)
            }

            else -> {}
        }

    }

    val color1 = if (!noUserSelected1) {
        if (userSelected1) {
            Color(0xFFDDDD50)
        } else {
            Color.White
        }
    } else {
        when (answerState1) {
            1 -> {
                Color.White
            }

            2 -> {
                Color(0xFF2A8B2E)
            }

            3 -> {
                Color(0xFFF44336)
            }

            else -> {}
        }

    }

    val color2 = if (!noUserSelected2) {
        if (userSelected2) {
            Color(0xFFDDDD50)
        } else {
            Color.White
        }
    } else {
        when (answerState2) {
            1 -> {
                Color.White
            }

            2 -> {
                Color(0xFF2A8B2E)
            }

            3 -> {
                Color(0xFFF44336)
            }

            else -> {}
        }

    }

    val color3 = if (!noUserSelected3) {
        if (userSelected3) {
            Color(0xFFDDDD50)
        } else {
            Color.White
        }
    } else {
        when (answerState3) {
            1 -> {
                Color.White
            }

            2 -> {
                Color(0xFF2A8B2E)
            }

            3 -> {
                Color(0xFFF44336)
            }

            else -> {}
        }
    }

    var selectedAnswer by rememberSaveable { mutableIntStateOf(0) }
    val showAnswer = remember { mutableStateOf(false) }
    val answerUI = remember { mutableStateOf(false) }

    if (reRunTimer.value) {
        LaunchedEffect(key1 = timeLeft) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
                if (timeLeft == 0) {
                    checkAnswer(
                        selectedAnswer,
                        questionList?.questions?.get(questionNumber.intValue)?.answerId,
                        validatedAnswer
                    )
                    if (validatedAnswer.value) {
                        numberOfCorrectAnswers.intValue += 1
                        showAnswer.value = true
                        answerUI.value = true
                    } else if (!validatedAnswer.value) {
                        showAnswer.value = false
                        answerUI.value = true
                    }
                    waitForAnswer.value = true
                }
            }
        }
    }

    if (waitForAnswer.value) {
        LaunchedEffect(validatedAnswer) {
            while (valuationTimeLeft > 0) {
                delay(1000L)
                valuationTimeLeft--
                if (valuationTimeLeft == 0) {
                    noUserSelected2 = false
                    noUserSelected = false
                    noUserSelected1 = false
                    noUserSelected3 = false
                    if (userSelected) {
                        userSelected = !userSelected
                    }
                    if (userSelected1) {
                        userSelected1 = !userSelected1
                    }
                    if (userSelected2) {
                        userSelected2 = !userSelected2
                    }
                    if (userSelected3) {
                        userSelected3 = !userSelected3

                    }
                    if (qno.intValue == 15) {
                        waitForAnswer.value = false
                        reRunTimer.value = false
                        nextQuestion.value = false
                        gameOverUI.value = true
                    } else {
                        nextQuestion.value = true
                        questionNumber.intValue++
                        qno.intValue++
                        timeLeft = 30
                        valuationTimeLeft = 10
                    }
                    reRunTimer.value = false
                    waitForAnswer.value = false
                }
            }
        }

    }
    Column {
        Box(
            modifier = Modifier
                .requiredHeight(80.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFFF5722))
        ) {

        }

        Column(
            modifier = Modifier
                .padding(5.dp)
                .requiredHeight(250.dp)
                .clip(shape = RoundedCornerShape(4))
                .background(color = Color(0xFFE2E2E2))
        ) {

            Row(
                modifier = Modifier
                    .requiredHeight(55.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFFE2E2E2)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .requiredWidth(80.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "00: $timeLeft",
                            fontStyle = FontStyle.Normal,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(50.dp, 0.dp, 20.dp, 0.dp))
                Text(
                    text = "FLAGS CHALLENGE",
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    color = Color(0xFFFF5722),
                    fontWeight = FontWeight.Bold
                )

            }
            Divider(color = Color(0xFFC7C3C3), thickness = Dp(1F))

            if (startChallengeUI.value) {
                StartChallengeUI(reRunTimer, nextQuestion, startChallengeUI)
            }
            if (nextQuestion.value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize(Alignment.CenterStart)
                            .size(50.dp)
                            .padding(0.dp, 5.dp, 5.dp, 0.dp)
                            .background(color = Color.Black)
                            .clip(shape = RoundedCornerShape(20.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(11.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = Color(0xFFFF5722), radius = 45F
                                    )
                                },
                            text = "" + qno.intValue,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "GUESS THE COUNTRY FROM THE FLAG ?",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(75.dp), shape = RoundedCornerShape(10)
                        ) {
                            Image(
                                painterResource(flagImage[questionNumber.intValue]),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Column(Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)) {
                            Row(modifier = Modifier.padding(2.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        selectedAnswer =
                                            questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                                0
                                            )?.id!!
                                        userSelected = !userSelected
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = color as Color),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .requiredHeight(33.dp)
                                        .requiredWidth(170.dp)
                                ) {
                                    Text(
                                        questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                            0
                                        )?.countryName!!,
                                        fontSize = 11.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (answerUI.value) {
                                        if (showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[0].id) {
                                                answerState = 2
                                                noUserSelected = true
                                            }
                                        } else if (!showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[0].id) {
                                                answerState = 3
                                                noUserSelected = true

                                            } else {
                                                if (questionList.questions[questionNumber.intValue].countries[0].id == questionList.questions[questionNumber.intValue].answerId) {
                                                    answerState = 2
                                                    noUserSelected = true
                                                } else {
                                                    answerState = 1
                                                    noUserSelected = true
                                                }
                                            }
                                        }

                                    }
                                }

                                Spacer(modifier = Modifier.padding(2.dp))

                                OutlinedButton(
                                    onClick = {
                                        selectedAnswer =
                                            questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                                1
                                            )?.id!!
                                        userSelected1 = !userSelected1
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = color1 as Color),
                                    shape = RoundedCornerShape(10.dp),
                                    //border = BorderStroke(1.dp, color = color1 as Color),
                                    modifier = Modifier
                                        .requiredHeight(33.dp)
                                        .requiredWidth(170.dp)
                                ) {
                                    Text(
                                        questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                            1
                                        )?.countryName!!,
                                        fontSize = 11.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    if (answerUI.value) {
                                        if (showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[1].id) {
                                                answerState1 = 2
                                                noUserSelected1 = true
                                            }
                                        } else if (!showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[1].id) {
                                                answerState1 = 3
                                                noUserSelected1 = true

                                            } else {
                                                if (questionList.questions[questionNumber.intValue].countries[1].id == questionList.questions[questionNumber.intValue].answerId) {
                                                    answerState1 = 2
                                                    noUserSelected1 = true
                                                } else {
                                                    answerState1 = 1
                                                    noUserSelected1 = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Row(modifier = Modifier.padding(2.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        selectedAnswer =
                                            questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                                2
                                            )?.id!!
                                        userSelected2 = !userSelected2
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = color2 as Color),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .requiredHeight(33.dp)
                                        .requiredWidth(170.dp)
                                ) {
                                    Text(
                                        questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                            2
                                        )?.countryName!!,
                                        fontSize = 11.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    if (answerUI.value) {
                                        if (showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[2].id) {
                                                answerState2 = 2
                                                noUserSelected2 = true
                                            }
                                        } else if (!showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[2].id) {
                                                answerState2 = 3
                                                noUserSelected2 = true

                                            } else {
                                                if (questionList.questions[questionNumber.intValue].countries[2].id == questionList.questions[questionNumber.intValue].answerId) {
                                                    answerState2 = 2
                                                    noUserSelected2 = true
                                                } else {
                                                    answerState2 = 1
                                                    noUserSelected2 = true
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.padding(2.dp))

                                OutlinedButton(
                                    onClick = {
                                        selectedAnswer =
                                            questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                                3
                                            )?.id!!
                                        userSelected3 = !userSelected3
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = color3 as Color),
                                    modifier = Modifier
                                        .requiredHeight(33.dp)
                                        .requiredWidth(170.dp)
                                ) {
                                    Text(
                                        questionList?.questions?.get(questionNumber.intValue)?.countries?.get(
                                            3
                                        )?.countryName!!,
                                        fontSize = 11.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    if (answerUI.value) {
                                        if (showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[3].id) {
                                                answerState3 = 2
                                                noUserSelected3 = true
                                            }
                                        } else if (!showAnswer.value) {
                                            if (selectedAnswer == questionList.questions[questionNumber.intValue].countries[3].id) {
                                                answerState3 = 3
                                                noUserSelected3 = true

                                            } else {
                                                if (questionList.questions[questionNumber.intValue].countries[3].id == questionList.questions[questionNumber.intValue].answerId) {
                                                    answerState3 = 2
                                                    noUserSelected3 = true
                                                } else {
                                                    answerState3 = 1
                                                    noUserSelected3 = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                reRunTimer.value = true
                answerUI.value = false
            }
            if (gameOverUI.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "GAME OVER",
                        fontSize = 35.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(4000)
                        gameOverUI.value = false
                        scoreUI.value = true
                    }
                }
            }
            if (scoreUI.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SCORE: ",
                            fontSize = 25.sp,
                            color = Color(0xFFFF5722),
                            fontWeight = FontWeight.Thin
                        )
                        Text(
                            text = "${calculateTotalScore(numberOfCorrectAnswers.intValue)}/100",
                            fontSize = 35.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StartChallengeUI(
    reRunTimer: MutableState<Boolean>,
    nextQuestion: MutableState<Boolean>,
    startChallengeUI: MutableState<Boolean>
) {
    var startChallengeTimer by remember { mutableIntStateOf(20) }

    LaunchedEffect(key1 = startChallengeTimer) {
        while (startChallengeTimer > 0) {
            delay(1000L)
            startChallengeTimer--
            if (startChallengeTimer == 0) {
                startChallengeUI.value = false
                nextQuestion.value = true
                reRunTimer.value = true
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CHALLENGE",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "WILL START IN",
            fontSize = 35.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "00:$startChallengeTimer",
            fontSize = 25.sp,
            color = Color(0xFFA59F9D),
            fontWeight = FontWeight.Medium
        )
    }
}


fun readJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

fun parseJsonToModel(jsonString: String): MainQuestion? {
    val gson = Gson()
    return gson.fromJson(jsonString, MainQuestion::class.java)
}

fun checkAnswer(
    optionSelected: Int, correctAnswer: Int?, validatedAnswer: MutableState<Boolean>
) {
    validatedAnswer.value = optionSelected == correctAnswer
}

fun addImages(flagImage: MutableList<Int>) {
    flagImage.add(R.drawable.newzealand)
    flagImage.add(R.drawable.aruba)
    flagImage.add(R.drawable.ecuador)
    flagImage.add(R.drawable.paraguay)
    flagImage.add(R.drawable.kyrgyzstan)
    flagImage.add(R.drawable.saintpierreandmiquelon)
    flagImage.add(R.drawable.japan)
    flagImage.add(R.drawable.turkmenistan)
    flagImage.add(R.drawable.gabon)
    flagImage.add(R.drawable.martinique)
    flagImage.add(R.drawable.belize)
    flagImage.add(R.drawable.czechrepublic)
    flagImage.add(R.drawable.unitedarabemirates)
    flagImage.add(R.drawable.jersey)
    flagImage.add(R.drawable.lesotho)
}

fun calculateTotalScore(answer: Int): Int {
    val correctAnswerFloat = answer.toFloat() / 15
    return (correctAnswerFloat * 100).toInt()
}
