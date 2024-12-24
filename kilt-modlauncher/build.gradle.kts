@Suppress("PropertyName")
val loader_version: String by rootProject

dependencies {
    compileOnly("net.fabricmc:fabric-loader:$loader_version")
}