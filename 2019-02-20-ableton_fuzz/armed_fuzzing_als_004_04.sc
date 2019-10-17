# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/ableton/fuzzing_samples/*)
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

        TestSample_Copy:
            MutatorGetCurrentSample
            Push
            GetInDir
            Push
            Str(copy )
            StrCat
            StrCat(\)
            StrCat
            StrCat( c:\users\john\desktop)
            RunCommand
            CheckStrStr(cannot)=(Y:TestSample_Sleep)
            CheckStrStr(network)=(Y:TestSample_Sleep)

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
        # TODO: generate & deploy batch
        MutatorGenerateBatch
        goto(TestSample_Start)

    TestSample_IgnoreAndContinue:
        Pause
        TracerDebugContinue
        goto(TestSample_decision)

    TestSample_decision:
        Decision=(
            GotLoaded:TestSample_Uninteresting,
            Aborted:TestSample_Uninteresting,
            EXCEPTION:TestSample_GotException,
            TIMEOUT:TestSample_Timeout,
            default:TestSample_IgnoreAndContinue
            )

    TestSample_GotException:
        TracerGetExceptionCode
        CheckStrStr(0xe06d7363)=(Y:TestSample_Continue)

        TracerGetExceptionAddressStr
        Call(FilterAddresses)
        CheckStrStrWQueue=(Y:TestSample_Uninteresting,N:TestSample_Interesting)

    TestSample_Continue:
        TracerDebugContinue(0x80010001)
        goto(TestSample_decision)

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
        Call(FuzzReload)
        #Call(Restart)
        goto(Fuzz_loop)

    Return

FuzzReload:
    QemuLoad(armed)

    RegisterReactions(self+0x169a9db,GotLoaded,0x0)
    EnableReaction(GotLoaded)

    RegisterReactions(self+0xea439b,Aborted,0x0)
    EnableReaction(Aborted)

    SetDebugTimeout(60000)

    Return

StartFuzz:
    QemuStart
    QemuLoad(armed)

    RegisterReactions(self+0x169a9db,GotLoaded,0x0)
    EnableReaction(GotLoaded)

    RegisterReactions(self+0xea439b,Aborted,0x0)
    EnableReaction(Aborted)

    SetDebugTimeout(60000)

    Return

ConfigureMutator:
    MutatorInit
    MutatorSelectChanger(ableton_als_changer_compressed_2)
    MutatorMutationCount(1)
    MutatorBatchSize(10)
    MutatorOriginal(original_7.als)
    MutatorExtension(als)
    MutatorDeployDir
    Return

FilterAddresses:
    Enqueue(0x00998987)
    Enqueue(0x011e5423)
    Enqueue(0x011f1dc3)
    Enqueue(0x018afff9)
    Enqueue(0x018b06c1)
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

