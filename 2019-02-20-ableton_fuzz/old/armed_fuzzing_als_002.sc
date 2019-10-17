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
            EXCEPTION:TestSample_GotCrash,
            default:TestSample_IgnoreAndContinue
            )

    TestSample_GotCrash:
        TracerGetExceptionCode
        CheckStrStr(0xe06d7363)=(N:TestSample_Interesting)

        TracerDebugContinue(0x80010001)
        goto(TestSample_decision)

        TestSample_Interesting:
            Call(SaveCrash)

        TestSample_Uninteresting:

        # will reload qemu
        Return

SaveCrash:
    # TODO: move sample to the saved directory
    MutatorSaveSample

    # construct description w original name, sample name, crash address, crash exc code
    TracerGetExceptionCode
    Push
    TracerGetExceptionAddress
    Push

    Push(2)
    MutatorSaveCrashData

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
    SetDebugTimeout(10000)

    Return

StartFuzz:
    QemuStart
    QemuLoad(armed)

    RegisterReactions(self+0x169a9db,GotLoaded,0x0)
    EnableReaction(GotLoaded)
    SetDebugTimeout(10000)

    Return

ConfigureMutator:
    MutatorInit
    MutatorSelectChanger(ableton_als_changer_compressed)
    MutatorMutationCount(1)
    MutatorBatchSize(10)
    MutatorOriginal(original_3.als)
    MutatorExtension(als)
    MutatorDeployDir
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

