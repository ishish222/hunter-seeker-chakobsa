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

TestUntilCrash:
    TestUntilCrash_TestNextSample:
        Call(RemoveLastSample)

        MutatorBatchExhausted=(Y:TestUntilCrash_Generate_Batch)

        MutatorGetNextSample

        TestUntilCrash_Copy:
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
            CheckStrStr(cannot)=(Y:TestUntilCrash_Sleep)

        MutatorGetCurrentSample
        Push
        Str(start c:\users\john\desktop\)
        StrCat
        RunCommand

        TracerDebugContinueInf
        goto(TestUntilCrash_decision)

    TestUntilCrash_Sleep:
        Wait(1)
        goto(TestUntilCrash_Copy)

    TestUntilCrash_Close:
        # Note: (does not apply to Ableton)
        goto(TestUntilCrash_TestNextSample)
        goto(TestUntilCrash_decision)

    TestUntilCrash_Generate_Batch:
        # TODO: generate & deploy batch
        MutatorGenerateBatch
        goto(TestUntilCrash_TestNextSample)

    TestUntilCrash_decision:
        Decision=(
            GotLoaded:TestUntilCrash_Close,
            GotClean:TestUntilCrash_TestNextSample,
            EXCEPTION:TestUntilCrash_GotCrash,
            default:TestUntilCrash_TestNextSample
            )

    TestUntilCrash_GotCrash:
        TracerGetExceptionCode
        CheckStrStr(0xe06d7363)=(N:TestUntilCrash_Interesting)

        TracerDebugContinueInf(0x80010001)
        goto(TestUntilCrash_decision)

        TestUntilCrash_Interesting:

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
        Call(TestUntilCrash)
        Call(SaveCrash)
        Call(Restart)
        goto(Fuzz_loop)

    Return

StartFuzz:
    #Call(QemuStartRevert)
    QemuStart
    QemuLoad(loaded)
    GetPIDByMatch(Trial)
    Push
    SetPID

    Call(AttachFileLog)

    RegisterReactions(self+0xe3cb68,ST,0x0)
    RegisterReactions(self+0xe3cb6a,GotLoaded,0x0)
    EnableReaction(ST)
    EnableReaction(GotLoaded)

    Return

ConfigureMutator:
    MutatorInit
    MutatorSelectChanger
    MutatorMutationCount(3)
    MutatorBatchSize(100)
    MutatorOriginal(original_1.alp)
    MutatorExtension(als)r
    MutatorDeployDir
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)
    Call(ConfigureMutator)
    Call(StartFuzz)
    Call(Fuzz)

    TracerDebugContinueInf
    goto(decision)

Default:
finish:
    QemuQuit

