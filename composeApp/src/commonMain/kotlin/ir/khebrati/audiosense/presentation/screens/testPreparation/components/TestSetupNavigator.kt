package ir.khebrati.audiosense.presentation.screens.testPreparation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TestSetupBottomBar(modifier: Modifier = Modifier, count: Int, onClick: () -> Unit) {
    HorizontalDivider(modifier = modifier)
    val pagerState = rememberPagerState(initialPage = 0) { count }
    HorizontalPager(pagerState){}
    Row(
        modifier = modifier.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Indicator(
            totalWidth = 100.dp,
            activeLineWidth = 36.dp,
            circleSpacing = 8.dp,
            width = 12.dp,
            height = 12.dp,
            pagerState= pagerState,
            radius = CornerRadius(x = 40f, y = 40f),
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.weight(1f))
        SkipButton()
        val scope = rememberCoroutineScope()
        NextButton(
            modifier = Modifier.height(60.dp).width(120.dp),
            enabled = true,
            isDone = pagerState.currentPage == count - 1,
            onClick = {
                val currentPage = pagerState.currentPage
                val nextPage = currentPage + 1
                if (nextPage <= count) {
                    scope.launch {
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
                onClick()
            },
        )
    }
}

@Preview(widthDp = 600)
@Composable
fun TestSetupBottomBarPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            TestSetupBottomBar(count = 4, modifier = Modifier.height(100.dp).fillMaxWidth().align(Alignment.BottomEnd), onClick = {})
        }
    }
}
