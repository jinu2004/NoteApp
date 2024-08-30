package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.project.data.Note
import org.example.project.domain.uiState.UiEvents
import org.example.project.domain.viewmodel.MainViewModel
import org.example.util.resource.Res
import org.example.util.resource.roboto_medium
import org.jetbrains.compose.resources.Font
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

class AddNewNote(private val navController: NavController) {

    @OptIn(KoinExperimentalAPI::class)
    @Composable
    fun View(noteId: String?) {

        val viewModel = koinViewModel<MainViewModel>()
        var categoryList by remember { mutableStateOf(emptyList<String>()) }
        var item by remember { mutableStateOf(Note()) }
        var backgroundColor by remember { mutableStateOf(item.color) }
        var selectedItem by remember { mutableStateOf(item.category) }
        var heading by remember { mutableStateOf(item.heading) }
        var notes by remember { mutableStateOf(item.note) }


        LaunchedEffect(Unit) {
            viewModel.state.collect {
                categoryList = it.filterData.toList()
            }
        }


        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var isWarning by remember { mutableStateOf(false) }


        if (noteId != "empty" && noteId != null) {
            viewModel.onEvent(UiEvents.GetNoteById(noteId))
            LaunchedEffect(Unit) {
                viewModel.state.collect {
                    if (it.itemInDetail != null) {
                        item = it.itemInDetail
                        backgroundColor = item.color
                        selectedItem = item.category
                        heading = item.heading
                        notes = item.note

                    }
                }
            }
        }



        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackBarHostState) })
        {


            Column(modifier = Modifier.fillMaxSize().background(color = Color(backgroundColor))) {
                var isExpandedDropDown by remember { mutableStateOf(false) }
                var isDialogOpen by remember { mutableStateOf(false) }
                if (isDialogOpen) {
                    selectedItem = CategoryDialog(
                        onConfirm = { isDialogOpen = !isDialogOpen },
                        onDismiss = { isDialogOpen = !isDialogOpen })
                }

                if (isWarning) {
                    Alert(onDismiss = { isWarning = !isWarning }, onConfirm = {
                        if (noteId != "empty" && noteId != null) {
                            viewModel.onEvent(UiEvents.DeleteNote(noteId))
                        }
                        notes = ""
                        heading = ""
                        selectedItem = ""
                        isWarning = !isWarning
                    })
                }


                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(0.6f)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(size = 20.dp)
                            ).border(
                                BorderStroke(1.dp, Color.Black),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { isExpandedDropDown = !isExpandedDropDown }
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedItem,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                )
                            )

                            IconButton(onClick = {
                                isExpandedDropDown = !isExpandedDropDown
                            }) { Icon(Icons.Filled.ArrowDropDown, "") }
                        }

                        DropdownMenu(
                            expanded = isExpandedDropDown,
                            onDismissRequest = { isExpandedDropDown = !isExpandedDropDown },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Add category",
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                            fontWeight = FontWeight(600),
                                            color = Color(0xFF000000),
                                        )
                                    )
                                },
                                onClick = { isDialogOpen = !isDialogOpen },
                                leadingIcon = { Icon(Icons.Filled.Add, "") },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            categoryList.forEach { string ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = string,
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                                fontWeight = FontWeight(600),
                                                color = Color(0xFF000000),
                                            )
                                        )
                                    },
                                    onClick = { selectedItem = string },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }


                        }
                    }

                    IconButton(onClick = { isWarning = !isWarning }) {
                        Icon(Icons.Outlined.Delete, "")
                    }
                }

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
                        .padding(horizontal = 10.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
                    border = BorderStroke(2.dp, color = Color.Black)
                ) {
                    TextField(
                        value = heading,
                        onValueChange = { heading = it },
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color.Black
                        ),
                        placeholder = {
                            Text(
                                text = "Heading",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color.Black
                                )

                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                            fontWeight = FontWeight(600),
                            color = Color.Black
                        )
                    )

                    TextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(
                                text = "Write your note",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color.Black
                                )

                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textDecoration = TextDecoration.None
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { navController.navigate(Screens.HomeScreen.route) }) {
                        Text(
                            text = "Cancel",
                            style = TextStyle(
                                fontSize = 32.sp,
                                fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )
                    }


                    OutlinedButton(
                        onClick = {
                            if (noteId != "empty") {
                                item.note = notes
                                item.heading = heading
                                item.category = selectedItem
                                item.id = noteId!!
                                item.color = backgroundColor
                                if (notes.isNotBlank() && heading.isNotBlank() && selectedItem.isNotBlank()) {
                                    viewModel.onEvent(UiEvents.UpdateNote(item))
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Add a Note and Select a category ",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }


                            } else {
                                item.note = notes
                                item.heading = heading
                                item.category = selectedItem
                                item.color = backgroundColor
                                if (notes.isNotBlank() && heading.isNotBlank() && selectedItem.isNotBlank() && selectedItem != "select one") {
                                    viewModel.onEvent(UiEvents.AddNewNote(item))
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Add a Note and Select a category ",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                            navController.navigate(Screens.HomeScreen.route)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Confirm",
                            style = TextStyle(
                                fontSize = 32.sp,
                                fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            ),
                            modifier = Modifier.padding(15.dp)
                        )
                    }

                }


            }
        }


    }

    @Composable
    fun CategoryDialog(onDismiss: () -> Unit, onConfirm: () -> Unit): String {

        var category by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().wrapContentSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add New Category",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF000000),
                        ), modifier = Modifier.padding(20.dp)
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        placeholder = {
                            Text(
                                text = "Write your note",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color.LightGray
                                )

                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textDecoration = TextDecoration.None
                        )
                    )


                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { onConfirm() }) {
                            Text(
                                text = "Confirm",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.roboto_medium)),
                                    fontWeight = FontWeight(600),
                                    color = Color.LightGray
                                )

                            )
                        }
                    }
                }
            }
        }
        return category
    }


    @Composable
    fun Alert(onConfirm: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text("Yes")
                }
            },
            text = { Text("Do you want to delete this note") },
            title = { Text("Warning") },
            icon = { Icon(Icons.Filled.Warning, "") },
            shape = RoundedCornerShape(20.dp),
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            },


            )
    }
}