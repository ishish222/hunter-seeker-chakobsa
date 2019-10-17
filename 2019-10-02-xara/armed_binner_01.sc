# acad binner
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
#    GlobPattern(_FUZZING_SAMPLES/xara/crashes_01/*xar)
#    GlobPattern(_FUZZING_SAMPLES/xara/crashes_02/*xar)
    GlobPattern(_FUZZING_SAMPLES/xara/03_crashes_01/*xar)
    HostDeployInputGlob

    Return

BinSample:
    TestSample_Start:

        BinnerGetNextSample

        BinSample_Copy:
            BinnerGetCurrentSample
            Push
            GetInDir
            Push
            Str(copy )
            StrCat
            StrCat(\)
            StrCat
            StrCat( c:\users\john\desktop)
            RunCommand
            CheckStrStr(cannot)=(Y:BinSample_Sleep)
            CheckStrStr(network)=(Y:BinSample_Sleep)

        BinnerGetCurrentSample
        Push
        Str(start c:\users\john\desktop\)
        StrCat
        RunCommand

        TracerDebugContinue
        goto(BinSample_decision)

    BinSample_Sleep:
        Wait(1)
        goto(BinSample_Copy)

    BinSample_IgnoreAndContinue:
        Pause
        TracerDebugContinue
        goto(BinSample_decision)

    BinSample_decision:
        Decision=(
            GotLoaded:BinSample_Uninteresting,
            Aborted:BinSample_Uninteresting,
            RE:BinSample_GotException,
            TO:BinSample_Timeout,
            default:BinSample_Inform
            )

    BinSample_GotException:
        TracerGetExceptionCode
        CheckStrStr(0xc0000004)=(Y:BinSample_Interesting)
        CheckStrStr(0xc0000005)=(Y:BinSample_Interesting)
        TracerGetExceptionChance
        CheckStrStr(0x00000000)=(Y:BinSample_Interesting)

    BinSample_Continue:
        TracerDebugContinue(0x80010001)
        goto(BinSample_decision)

    BinSample_Inform:
        Beep(Xara%20binning%20unexpected%20behavior)
        Pause
        Continue=
        goto(BinSample_decision)

    BinSample_Interesting:
        Call(BinCrash)
        goto(BinSample_finish)

    BinSample_Timeout:
        Call(BinFailed)
        BinnerReportTimeout
        goto(BinSample_finish)

    BinSample_Uninteresting:
        Call(BinFailed)
        goto(BinSample_finish)

    # will reload qemu
    BinSample_finish:
    Return

BinCrash:
    TracerGetExceptionAddressStr
    Push
    BinnerConfirmSample

    Return

BinFailed:
    BinnerUnconfirmSample

    Return

Restart:
    QemuQuit
    Call(StartBinning)
    Return

Binning:
    Binning_loop:
        BinnerBatchExhausted=(Y:Binning_finish)
        Call(BinSample)
        BinnerReport
        Call(BinningReload)
        goto(Binning_loop)

    Binning_finish:
    Return

EnableNecessaryReactions:
    RegisterReactions(self+0xae8850,GotLoaded,0x0)
    EnableReaction(GotLoaded)

    SetDebugTimeout(20000)

    Return

BinningReload:
    QemuLoad(armed)
    Call(EnableNecessaryReactions)
    Return

StartBinning:
    QemuStart
    QemuLoad(armed)
    Call(EnableNecessaryReactions)
    Return

ConfigureBinner:
    BinnerInit
    BinnerLoadSamples
    Return

Main:
    ResetTime
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)
    Call(ConfigureBinner)
    Call(StartBinning)
    Call(Binning)

    goto(finish)

RC:
    FlushFiles
    Beep(Tracer%20crashed)
    Pause

Default:
    Continue=

Exception:
    Beep(Binner%20exception)
RX:
    ReadTime
    Beep(Xara%20binning%20finished)
finish:
    #FlushFiles
    BinnerReport
    QemuQuit

