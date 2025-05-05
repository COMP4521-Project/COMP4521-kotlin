package com.example.comp4521_ustrade.app.components

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.display.DisplayCourseCards
import com.example.comp4521_ustrade.app.viewmodel.CourseViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.ui.theme.USTWhite_dark
import com.example.comp4521_ustrade.ui.theme.USTgray

//Home Page

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseMenu(courseViewModel:CourseViewModel, navigateController: NavController) {
//    val courseCodes = listOf(
//        "ACCT", "AESF", "AIAA", "AISC", "AMAT", "BEHI", "BIBU", "BIEN",
//        "BSBE", "BTEC", "CENG", "CHEM", "CHMS", "CIEM", "CIVL", "CMAA",
//        "COMP", "CPEG", "CSIC", "CSIT", "CTDL", "DASC", "DBAP", "DRAP",
//        "DSAA", "DSCT", "ECON", "EEMT", "EESM", "ELEC", "EMBA", "EMIA",
//        "ENEG", "ENGG", "ENTR", "ENVR", "ENVS", "EOAS", "EVNG", "EVSM",
//        "FINA", "FTEC", "GBUS", "GFIN", "GNED", "HLTH", "HMAW", "HMMA",
//        "HUMA", "IBTM", "IEDA", "IIMP", "IMBA", "INTR", "IOTA", "IPEN",
//        "ISDN", "NISO", "MJEV", "ELAB", "ULAN", "GLIF", "SMAE", "DMAF",
//        "SMAR", "KMAS", "SMAT", "HMCE", "EMEC", "HMES", "FMIT", "MGCS",
//        "MGMT", "MICS", "MILE", "MIMM", "MSBD", "MSDM", "MTLE", "NANO",
//        "OCES", "PDEV", "PHYS", "PPOL", "RMBI", "ROAS", "SBMT", "SCIE",
//        "SEEN", "SHSS", "SMMG", "SOSC", "SUST", "TEMG", "UCOP", "UGOD",
//        "UPOP", "UROP", "UTOP", "WBBA"
//    )

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }


    val courseTitles = courseViewModel.courseTitles

    val pagerState = rememberPagerState {  courseTitles.size }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val modifier: Modifier = Modifier

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier.padding(8.dp),
            containerColor = Color.Transparent,
            edgePadding = 8.dp,
            indicator =  { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = USTBlue,
                )
            },
            divider = {
                Divider(
                    color = Color.LightGray,
                )
            },
            ) {
            courseTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                    },
                    selectedContentColor = USTBlue,
                    unselectedContentColor = if(isDarkModeEnabled) USTWhite_dark else Color.Black,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                )
            }
        }


        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            Box(contentAlignment = Alignment.Center) {
                DisplayCourseCards(navigateController=navigateController, subject = courseTitles[page])
            }

//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Text(
//                    text = courseTitles[page], // Display the tab title
//                    style = MaterialTheme.typography.bodyLarge
//                )
//            }
        }
    }
}
