package ir.khebrati.audiosense.presentation.screens.testPreparation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TestSetupBottomBar(modifier: Modifier = Modifier, count: Int, onClick: () -> Unit) {
    HorizontalDivider(modifier = modifier)
    val pagerState = rememberPagerState(initialPage = 0) { count }
    HorizontalPager(pagerState){}
    Row(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Indicator(
            totalWidth = 50.dp,
            activeLineWidth = 30.dp,
            circleSpacing = 3.dp,
            width = 10.dp,
            height = 10.dp,
            pagerState = pagerState,
            radius = CornerRadius(x = 20f, y = 20f),
            color = MaterialTheme.colorScheme.primary,
        )
        val scope = rememberCoroutineScope()
        NextButton(
            enabled = true,
            isDone = pagerState.currentPage == count - 1,
            onClick = {
                val currentPage = pagerState.currentPage
                val nextPage = currentPage + 1
                if (nextPage <= count) {
                    scope.launch {
                        println("scrolling the page: $nextPage")
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
                onClick()
            },
        )
    }
}

@Preview(widthDp = 400)
@Composable
fun TestSetupBottomBarPreview() {
    TestSetupBottomBar(count = 3, modifier = Modifier.fillMaxWidth(), onClick = {})
}
