package xyz.bluspring.kilt.loader

import de.florianmichael.asmfabricloader.api.event.PrePrePreLaunchEntrypoint

class KiltEarlierInitializer : PrePrePreLaunchEntrypoint {
    override fun onLanguageAdapterLaunch() {
        KiltLoader.INSTANCE.scanModJob
        KiltLoader.INSTANCE.runScanMods()
    }
}