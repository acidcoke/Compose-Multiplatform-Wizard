package wizard

import wizard.dependencies.ApolloPlugin
import wizard.files.*
import wizard.files.composeApp.*

fun ProjectInfo.generateComposeAppFiles() = buildList {
    val info = this@generateComposeAppFiles

    add(Gitignore())
    add(Readme(info))

    add(GradleBat())
    add(Gradlew())
    add(GradleWrapperProperties(info))
    add(GradleWrapperJar())
    add(GradleLibsVersion(info))

    add(GradleProperties(true))
    add(RootBuildGradleKts(info))
    add(SettingsGradleKts(info, false))

    add(ModuleBuildGradleKts(info))
    add(ColorKt(info))
    add(ThemeKt(info))
    add(AppKt(info))

    if (info.dependencies.contains(ApolloPlugin)) {
        add(GraphQLSchema(info))
        add(GraphQLQuery(info))
    }

    if (info.hasPlatform(ProjectPlatform.Android)) {
        add(AndroidManifest(info))
        add(AndroidAppKt(info))
        add(AndroidThemeKt(info))
    }

    if (info.hasPlatform(ProjectPlatform.Jvm)) {
        add(DesktopAppKt(info))
        add(DesktopMainKt(info))
        add(DesktopThemeKt(info))
    }

    if (info.hasPlatform(ProjectPlatform.Ios)) {
        add(IosAppKt(info))
        add(IosMainKt(info))
        add(IosThemeKt(info))

        add(IosAppIcon())
        add(IosAccentColor())
        add(IosAssets())
        add(IosPreviewAssets())
        add(IosAppSwift())
        add(IosXcworkspace())
        add(IosPbxproj(info))
    }

    if (info.hasPlatform(ProjectPlatform.Js)) {
        add(BrowserAppKt(info))
        add(IndexHtml(info))
        add(BrowserMainKt(info))
        add(BrowserThemeKt(info))
        add(WebPackConfig(info))
    }
}