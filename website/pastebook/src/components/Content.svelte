<script lang="ts">
    export let content: string = "No content provided"
    export let reportBook: boolean = false

    let flaggedText = [
        "mfemms.exe",
        "ASCTray.exe",
        "BoostSpeed.exe",
        "acs.exe",
        "RPXAcceleratePro.exe",
        "cliff-3.2.0-12297.xpi",
        "FileOpenerWindows.exe",
        "CovenantEyes.exe",
        "AVG",
        "authserver.mojang.com",
        "sessionserver.mojang.com",
        "Diebold\\Warsaw",
        "McAfee\\VirusScan Enterprise",
        "Norton",
        "ByteFence",
        "netsession_win.exe",
        "COMODO",
        "Intel Security",
        "PremierOpinion",
        "AnonineVPN",
        "SDFSSvc.exe",
        "Bitdefender",
        "esif_assist.exe",
        "Hamachi",
        "SecureLine\\VpnSvc",
        "CyberGhost",
        "Kapersky",
        "PCKeeper",
        "Evolve",
        "Avira.exe",
        "Avira.VpnService",
        "AvastSvc",
        "Sophos",
        "BFNService.exe",
        "TnglCtrl",
        "hidemesvc.exe",
        "ovpnagent.exe",
        "AdVpnService.exe",
        "VPNUnlimitedService",
        "GlassWire.exe",
        "ZAM.exe",
        "F-Secure",
        "Overwolf",
        "nordvpn-service.exe",
        "Trusteer",
        "RzSynapse.exe",
        "WindscribeService.exe",
        "NOD32",
        "IDMan.exe",
        "KMSpico",
        "Duet Display",
        "AIPS.exe",
        "ipts.exe",
        "rlvknlg.exe",
        "hsscp.exe",
        "k9filter.exe",
        "OkayFreedom",
        "rsService.exe",
        "Plays.exe",
        "TunnelBear",
        "IObit",
        "Brave",
        "RestoroProtection",
        "PlayerGC32.exe",
        "Speedify.exe",
        "WebDiscoverBrowser",
        "Shiginima",
        "HideMy.name VPN",
        "ZaxarLoader.exe",
        "SurfEasyVPN.exe",
        "opnsqr.exe",
        "ekrn.exe",
        "ZenMate.bat",
        "Zonealarm",
        "BullGuard",
        "VyprVPN",
        "pia-service.exe",
        "ProtonVPN.exe",
        "vpnagent.exe",
        "RvRvpnGui.exe",
        "vpn",
        "Medal.exe",
        "adguardianplusprotection.exe",
        "Segurazo",
        "altening.launcher.exe",
        "Trust.Zone",
        "Qustodio",
        "tlauncher",
        "Quick Heal Antivirus",
        "ProtonVPNService.exe",
        "RTSS.exe",
        "SAntivirus",
        "Surfshark.exe",
        "KACE Agent",
        "LskHelper.exe",
        "\\pservice.exe",
        "IOCP.exe",
        "K7TSecurity",
        "AtlasVPN.Worker.exe",
        "McCHSvc.exe"

    ]

    let contentLines = content.split("\n")

    function scanContent(content: String) {
        if (reportBook === false) {
            return false;
        }

        for (let string of flaggedText) {
            if (content.toLowerCase().includes(string.toLocaleLowerCase())) {
                return true;
            }
        }

        return false;
    }
</script>

<contentcontainer>
    <p>
        {#each contentLines as line}
            <linecontainer class="flagged-{scanContent(line)}">
                {line}
            </linecontainer>
            <br>
        {/each}
    </p>
</contentcontainer>

<style lang="scss">
    contentcontainer {
        display: inline-block;
        background-color: #eeeeee;
        width: calc(100% - 20px);
        margin: 10px;
        border-radius: 20px;
        animation: fadeIn ease 0.7s;
        animation-delay: 0.3s;
        animation-iteration-count: 1;
        animation-fill-mode: forwards;
        opacity: 0;
        overflow: auto;
    }

    p {
        display: inline-block;
        font-size: 13px;
        white-space: pre;
        font-family: "JetBrains Mono NL", monospace;
        margin: 20px;
        padding: 10px;

        .flagged-true {
            color: red;
        }
    }

    @keyframes fadeIn {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    }
</style>