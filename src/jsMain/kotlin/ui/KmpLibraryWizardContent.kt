package ui

import mui.icons.material.*
import mui.material.*
import mui.material.Size
import mui.material.Stack
import mui.system.Container
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.onChange
import web.cssom.*
import web.html.HTMLInputElement
import wizard.*
import wizard.ProjectPlatform.*
import wizard.dependencies.*
import mui.icons.material.Android as AndroidIcon

val KmpLibraryWizardContent = FC<AppProps> { props ->
    Container {
        sx {
            padding = Padding(24.px, 24.px)
            minWidth = 650.px
        }

        ShowVersionContext.Provider {
            value = useState(false)

            Paper {
                sx {
                    padding = Padding(24.px, 24.px)
                }

                TopMenu()

                Stack {
                    direction = responsive(StackDirection.column)
                    spacing = responsive(2)
                    sx {
                        alignItems = AlignItems.center
                    }

                    Header {
                        image = "kotlin-logo.svg"
                        title = "KMP Library Wizard"
                    }

                    val default = DefaultKmpLibraryInfo()
                    val textFieldWidth = 565.px

                    var projectName by useState(default.name)
                    TextField {
                        label = ReactNode("Project name")
                        sx {
                            width = textFieldWidth
                        }
                        value = projectName
                        onChange = { event ->
                            projectName = (event.target as HTMLInputElement).value
                        }
                    }

                    var projectId by useState(default.packageId)
                    TextField {
                        label = ReactNode("Project ID")
                        sx {
                            width = textFieldWidth
                        }
                        value = projectId
                        onChange = { event ->
                            projectId = (event.target as HTMLInputElement).value
                        }
                    }

                    var moduleName by useState(default.moduleName)
                    TextField {
                        label = ReactNode("Library name")
                        sx {
                            width = textFieldWidth
                        }
                        value = moduleName
                        onChange = { event ->
                            moduleName = (event.target as HTMLInputElement).value
                        }
                    }

                    var platforms by useState(setOf(Android, Ios, Jvm, Js))
                    fun switch(platform: ProjectPlatform) {
                        platforms = if (platforms.contains(platform)) {
                            platforms - platform
                        } else {
                            platforms + platform
                        }
                    }

                    ButtonGroup {
                        disableElevation = true
                        TargetButton {
                            title = Android.title
                            isSelected = platforms.contains(Android)
                            onClick = { switch(Android) }
                            icon = AndroidIcon
                        }
                        TargetButton {
                            title = Jvm.title
                            isSelected = platforms.contains(Jvm)
                            onClick = { switch(Jvm) }
                            icon = Laptop
                        }
                        TargetButton {
                            title = Ios.title
                            isSelected = platforms.contains(Ios)
                            onClick = { switch(Ios) }
                            icon = Apple
                        }
                        TargetButton {
                            title = Js.title
                            isSelected = platforms.contains(Js)
                            onClick = { switch(Js) }
                            icon = Language
                        }
                    }
                    ButtonGroup {
                        disableElevation = true
                        TargetButton {
                            title = Macos.title
                            isSelected = platforms.contains(Macos)
                            onClick = { switch(Macos) }
                            icon = ViewCarousel
                        }
                        TargetButton {
                            title = Linux.title
                            isSelected = platforms.contains(Linux)
                            onClick = { switch(Linux) }
                            icon = Engineering
                        }
                        TargetButton {
                            title = Mingw.title
                            isSelected = platforms.contains(Mingw)
                            onClick = { switch(Mingw) }
                            icon = Window
                        }
                        TargetButton {
                            title = Wasm.title
                            isSelected = platforms.contains(Wasm)
                            onClick = { switch(Wasm) }
                            icon = Preview
                            status = "Experimental"
                        }
                    }

                    VersionsTable {
                        sx {
                            width = textFieldWidth
                        }
                        info = default
                    }

                    val deps: Set<DependencyBox> = setOf(
                        DependencyBox(KotlinxCoroutinesCore, false),
                        DependencyBox(KotlinxSerializationJson, false),
                        DependencyBox(KotlinxDateTime, false),
                        DependencyBox(Kermit, false),
                        DependencyBox(KtorCore, false),
                        DependencyBox(SQLDelightPlugin, false),
                        DependencyBox(MultiplatformSettings, false),
                        DependencyBox(listOf(BuildConfigPlugin, BuildKonfigPlugin), false),
                        DependencyBox(listOf(Koin, Kodein), false),
                        DependencyBox(KStore, false),
                    )
                    Grid {
                        sx {
                            justifyContent = JustifyContent.spaceAround
                        }
                        spacing = responsive(2)
                        container = true
                        deps.forEach { dep ->
                            Grid {
                                item = true
                                DependencyCard {
                                    dependency = dep
                                }
                            }
                        }
                    }

                    Button {
                        variant = ButtonVariant.contained
                        size = Size.large
                        startIcon = ArrowCircleDown.create()
                        +"Download"

                        disabled = projectName.isBlank()
                                || projectId.isBlank()
                                || moduleName.isBlank()
                                || platforms.isEmpty()

                        onClick = {
                            val info = default.copy(
                                packageId = projectId,
                                name = projectName,
                                moduleName = moduleName,
                                platforms = platforms,
                                dependencies = buildSet {
                                    add(KotlinPlugin)
                                    if (platforms.contains(Android)) {
                                        add(AndroidLibraryPlugin)
                                    }
                                    addAll(deps.getSelectedDependencies())
                                }
                            )
                            props.generate(info)
                        }
                    }
                }
            }
        }
    }
}
