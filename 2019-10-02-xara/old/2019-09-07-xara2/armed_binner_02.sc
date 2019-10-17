# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    #GlobPattern(_FUZZING_SAMPLES/xara/crashes_01/*xar)
    GlobPattern(_FUZZING_SAMPLES/xara/bin_test/*xar)
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
            TIMEOUT:BinSample_Timeout,
            default:BinSample_IgnoreAndContinue
            )

    BinSample_GotException:
        TracerGetExceptionCode
        CheckStrStr(0xe06d7363)=(N:BinSample_Interesting)

        TracerDebugContinue(0x80010001)
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
    # TODO: move sample to the saved directory
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

BinningReload:
    QemuLoad(armed)

    RegisterReactions(self+0xae7495,GotLoaded,0x0)
    EnableReaction(GotLoaded)

#    RegisterReactions(self+0xea439b,Aborted,0x0)
#    EnableReaction(Aborted)

    SetDebugTimeout(15000)

    Return

StartBinning:
    QemuStart
    QemuLoad(armed)

    RegisterReactions(self+0xae7495,GotLoaded,0x0)
    EnableReaction(GotLoaded)

#    RegisterReactions(self+0xea439b,Aborted,0x0)
#    EnableReaction(Aborted)

    SetDebugTimeout(15000)

    Return

ConfigureBinner:
    BinnerInit
    BinnerLoadSamples
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)
    Call(ConfigureBinner)
    Call(StartBinning)
    Call(Binning)

    TracerDebugContinue
    goto(decision)

Default:
finish:
    BinnerReport
    QemuQuit

