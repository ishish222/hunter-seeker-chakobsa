# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/xara/02_corpus_01/*)
    HostDeployInputGlob

    Return

RemoveLastSample:
    MutatorGetCurrentSample
    CheckStrStr(None)=(Y:RemoveLastSample_end)
    Push
    Str(del c:\users\john\desktop\)
    StrCat
    RunCommand

    MutatorCurrentSampleDrop

    RemoveLastSample_end:
    Return

TestSample:
        Call(RemoveLastSample)

        MutatorBatchExhausted=(Y:TestSample_Generate_Batch)

    TestSample_Start:

        MutatorGetNextSample

        MutatorGetCurrentSample
        Push
        Str(start c:\users\john\desktop\)
        StrCat
        RunCommand

        TracerDebugContinue

        goto(TestSample_decision)

    TestSample_Sleep:
        Wait(1)
        goto(TestSample_Copy)

    TestSample_Generate_Batch:
        Call(FuzzReload)
        goto(TestSample_Start)

    TestSample_decision:
        Decision=(
            GotLoaded:TestSample_Uninteresting,
            Aborted:TestSample_Uninteresting,
            RE:TestSample_GotException,
            TO:TestSample_Timeout,
            default:TestSample_Inform
            )

    TestSample_GotException:
        TracerGetExceptionCode
        CheckStrStr(0xc0000004)=(Y:TestSample_Verify)
        CheckStrStr(0xc0000005)=(Y:TestSample_Verify)
        TracerGetExceptionChance
        CheckStrStr(0x00000000)=(Y:TestSample_Verify)

        TracerDebugContinue(0x80010001)
        goto(TestSample_decision)

    TestSample_Verify:
        Call(FilterAddresses)
        TracerGetExceptionAddressStr
        Push
        CheckStrStrWQueue=(Y:TestSample_Interesting,N:TestSample_Interesting_Notify)

    TestSample_Inform:
        Beep(Xara%20fuzzing%20unexpected%20behavior)
        Pause
        Continue=
        goto(TestSample_decision)

    TestSample_Continue:
        TracerDebugContinue(0x80010001)
        goto(TestSample_decision)

    TestSample_Interesting_Notify:
        Beep(New%20crash%20in%20Xara%20fuzzing)

    TestSample_Interesting:
        Call(BinCrash)
        goto(TestSample_finish)

    TestSample_Timeout:
        MutatorReportTimeout
        goto(TestSample_finish)

    TestSample_Uninteresting:
        goto(TestSample_finish)

    # will reload qemu
    TestSample_finish:
        Return

BinCrash:
    TracerGetExceptionAddressStr
    Push
    MutatorConfirmSample
    Return

Restart:
    QemuQuit
    Call(StartFuzz)
    Return

Fuzz:
    Fuzz_loop:
        Call(TestSample)
        MutatorReport
        #Call(FuzzReload)
        #Call(Restart)
        QemuQuickLoad
        goto(Fuzz_loop)

    Return

FuzzReload:
    QemuLoad(armed)

    RegisterReactions(self+0xae8850,GotLoaded,0x0)
    EnableReaction(GotLoaded)

#    RegisterReactions(self+0xea439b,Aborted,0x0)
#    EnableReaction(Aborted)

    SetDebugTimeout(10000)

    MutatorGenerateBatch
    FuzzReload_Copy:
        GetInDir
        Push
        Str(copy )
        StrCat
        StrCat(\)
        StrCat( c:\users\john\desktop)
        RunCommand
        CheckStrStr(cannot)=(Y:FuzzReload_Sleep)

    QemuQuickSave
    Return

    FuzzReload_Sleep:
        Wait(1)
        goto(FuzzReload_Copy)


StartFuzz:
    QemuStart
    QemuLoad(armed)

    RegisterReactions(self+0xae8850,GotLoaded,0x0)
    EnableReaction(GotLoaded)

#    RegisterReactions(self+0xea439b,Aborted,0x0)
#    EnableReaction(Aborted)

    SetDebugTimeout(10000)

    MutatorGenerateBatch
    FuzzReload_Copy:
        GetInDir
        Push
        Str(copy )
        StrCat
        StrCat(\)
        StrCat( c:\users\john\desktop)
        RunCommand
        CheckStrStr(cannot)=(Y:FuzzReload_Sleep)

    QemuQuickSave
    Return

    FuzzReload_Sleep:
        Wait(1)
        goto(FuzzReload_Copy)

ConfigureMutator:
    MutatorInit
#    MutatorSelectChanger(xar_raw_001)
    MutatorSelectChanger(changer)
    MutatorMutationCount(10)
    MutatorBatchSize(100)
    MutatorOriginal(origin03.xar)
    MutatorExtension(xar)
    MutatorDeployDir
    Return

FilterAddresses:
    Enqueue(0x00702d56)
    Enqueue(0x007d60b2)
    Enqueue(0x00aa6434)
    Enqueue(0x00c706c9)
    Enqueue(0x77eebc70)
    Enqueue(0x77f15b44)
    Enqueue(0x77f1699a)
    Enqueue(0xac858900)
    Enqueue(0x00c702de)
    PrintQueue
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)
    Call(ConfigureMutator)
    Call(StartFuzz)
    Call(Fuzz)

    TracerDebugContinue
    goto(decision)

Default:
finish:
    MutatorReport
    QemuQuit

