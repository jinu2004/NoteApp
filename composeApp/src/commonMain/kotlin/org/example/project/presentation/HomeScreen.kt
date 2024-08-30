package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.example.project.data.Note
import org.example.project.domain.uiState.UiEvents
import org.example.project.domain.viewmodel.MainViewModel
import org.example.util.resource.Res
import org.example.util.resource.grid_view
import org.example.util.resource.list_view
import org.example.util.resource.roboto_medium
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

class HomeScreen(private val navController: NavController) {

    @OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
    @Composable
    fun View() {

        val viewModel = koinViewModel<MainViewModel>()

        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = Screens.DetailNote.withArgs("empty")) },
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = RoundedCornerShape(20.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 5.dp),
                modifier = Modifier.border(
                    BorderStroke(width = 3.dp, Color.Black),
                    shape = RoundedCornerShape(20.dp)
                ).width(80.dp).height(80.dp)
            ) {
                Icon(Icons.Outlined.Add, "", modifier = Modifier.size(50.dp))
            }
        }, floatingActionButtonPosition = FabPosition.Center) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                @Suppress("LocalVariableName") val GRID_VIEW = 180
                @Suppress("LocalVariableName") val LIST_VIEW = 200

                var lazyView by remember { mutableStateOf(GRID_VIEW) }
                var searchQuery by remember { mutableStateOf("") }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp, end = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Notes",
                        style = TextStyle(
                            fontSize = 48.sp,
                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF000000),
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 35.dp, start = 25.dp, end = 35.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    SearchBar(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                        },
                        onSearch = {},
                        onActiveChange = { },
                        active = false,
                        leadingIcon = { Icon(Icons.Outlined.Search, "") },
                        content = {},
                        shape = RoundedCornerShape(20.dp),
                        colors = SearchBarDefaults.colors(containerColor = Color(0xFFD9D9D9)),
                        modifier = Modifier.fillMaxWidth(0.7f),
                        placeholder = {
                            Text(
                                text = "Search",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0x87000000),
                                )
                            )
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        IconButton(onClick = { lazyView = GRID_VIEW }) {
                            Icon(
                                painterResource(Res.drawable.grid_view),
                                "",
                                tint = if (lazyView == GRID_VIEW) Color.Black else Color.Gray
                            )
                        }
                        IconButton(onClick = { lazyView = LIST_VIEW }) {
                            Icon(
                                painterResource(Res.drawable.list_view),
                                "",
                                tint = if (lazyView == LIST_VIEW) Color.Black else Color.Gray
                            )
                        }
                    }

                }


                var category by remember { mutableStateOf(emptyList<String>()) }
                var listOfNote by remember { mutableStateOf(emptyList<Note>()) }
                LaunchedEffect(Unit) {
                    viewModel.state.collect {
                        category = it.filterData.toList()
                        listOfNote = it.note
                    }
                }



                var selectedItemIndex by remember { mutableIntStateOf(category.size + 1) }


                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, top = 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    if (category.isNotEmpty()) {
                        item {
                            Card(
                                onClick = {
                                    selectedItemIndex = category.size + 1
                                    viewModel.onEvent(UiEvents.FilterAction("All"))
                                },
                                shape = RoundedCornerShape(size = 15.dp),
                                border = BorderStroke(width = 2.dp, color = Color.Black),
                                modifier = Modifier.wrapContentSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedItemIndex == category.size + 1) Color(
                                        0xFFD3FC8F
                                    ) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = "All",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF000000),
                                    ),
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 12.dp
                                    )
                                )
                            }
                        }
                    }
                    category.forEachIndexed { index, it ->
                        item {
                            Card(
                                onClick = {
                                    selectedItemIndex = index
                                    viewModel.onEvent(UiEvents.FilterAction(it))
                                },
                                shape = RoundedCornerShape(size = 15.dp),
                                border = BorderStroke(width = 2.dp, color = Color.Black),
                                modifier = Modifier.wrapContentSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedItemIndex == index) Color(
                                        0xFFD3FC8F
                                    ) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = "#$it",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF000000),
                                    ),
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 12.dp
                                    )
                                )
                            }
                        }
                    }
                }




                if (listOfNote.isNotEmpty()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(lazyView.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, top = 15.dp, bottom = 10.dp)
                            .clip(
                                RoundedCornerShape(30.dp)
                            )
                            .height(1000.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalItemSpacing = 20.dp,
                    ) {
                        val filterData = listOfNote.filter {
                            it.heading.contains(searchQuery, ignoreCase = true)
                                    || it.note.contains(searchQuery, ignoreCase = true)
                        }

                        items(filterData) {
                            val color by remember { mutableStateOf(Color(it.color)) }
                            Card(
                                onClick = {
                                    navController.navigate(Screens.DetailNote.withArgs(it.id))
                                    println("id = ${it.id}")
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = color),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = it.heading,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF000000),
                                    ),
                                    modifier = Modifier.padding(
                                        horizontal = 15.dp,
                                        vertical = 15.dp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = it.note,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                        fontWeight = FontWeight(600),
                                        color = Color(0x8F000000),
                                    ),
                                    modifier = Modifier.padding(
                                        horizontal = 15.dp,
                                        vertical = 15.dp
                                    ),
                                    maxLines = 10,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }


                        }

                    }
                } else {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = "You don't Have any notes Please create some",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF000000),
                            ), modifier = Modifier.align(Alignment.Center).padding(80.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }


            }
        }

    }
}