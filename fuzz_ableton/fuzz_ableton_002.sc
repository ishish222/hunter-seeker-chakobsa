# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    GlobPattern(fuzz_ableton/*)
    HostDeployInputGlob

    Return

DeploySamples:
    Return

TestUntilCrash:
    TestUntilCrash_TestNextSample:

        MutatorGetCurrentSample
        Push
        Str(del c:\users\john\desktop\)
        StrCat
        RunCommand

        MutatorCurrentSampleDrop

        MutatorBatchExhausted=(Y:TestUntilCrash_Generate_Batch)

        MutatorGetNextSample

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

        MutatorGetCurrentSample
        Push
        Str(start c:\users\john\desktop\)
        StrCat
        RunCommand

        TracerDebugContinueInf
        goto(TestUntilCrash_decision)

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
    Call(QemuStartRevert)
    SetPID(0x000009a0)
    Call(Attach)

    TracerRegisterReactions(self+0xe932a9,GotLoaded,0x0)

    Return

ConfigureMutator:
    MutatorInit
    MutatorSelectChanger
    MutatorMutationCount(3)
    MutatorBatchSize(100)
    MutatorOriginal(original_1.als)
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

    TracerDebugContinueInf
    goto(decision)

change_to_suspended:
    Call(SuspendCreatedRemoteThread)

    TracerDebugContinueInf
    goto(decision)

set_priority:
    ReadRegister(ESP)
    Adjust(0x4)
    Push
    WriteDword(0x0)

    TracerDebugContinueInf
    goto(decision)

get_key_1:
    ReadArgUni(0x2)
    Enqueue

    TracerDebugContinueInf
    goto(decision)

deploy_val_1:

    DisableReaction(deploy_val_1)
    ReadStack
    ReadRegister(ESP)
    Adjust(0x4)
    Push

    ReadDword
    Push

    ReadUnicode
    Enqueue

    Enqueue(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)

    Str(reg add HKCU\)
    StrCatQueue
    StrCat( /v ) 
    StrCatQueue
    StrCat( /t REG_SZ /d )
    StrCatQueue

    Push
    RunCommand

    ListTebs
    CurrentTID
    Push
    SuspendAllExcept
    ListTebs

    Wait(10)
    TracerDebugContinueInf
    goto(decision)

read_from_reg_1_check_1:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

retrieve_data_2_check_3:
    ReadRegister(EAX)
    ReadRegister(EDX)
    TracerDebugContinueInf
    goto(decision)

zero_wait:
    ReadRegister(ESP)
    Adjust(0xc)
    Push
    WriteDword(0x0)

    ReadStack
    TracerDebugContinueInf
    goto(decision)

zero_wait_2:
    #ReadRegister(ESP)
    #Push
    #WriteDword(0x0)

    ReadRegister(ESP)
    Adjust(0x4)
    Push
    WriteDword(0x0)

    ReadStack
    TracerDebugContinueInf
    goto(decision)

fix_wait:
    Push(0x1)
    WriteRegister(EAX) 
    TracerDebugContinueInf
    goto(decision)

A1:
    ResolveLocation(EAX+0x185)
    Push2
    TracerDebugContinueInf
    goto(decision)
 
A2:
    Pop2
    Push
    TracerRegisterReactionsAt(A3,0x0)
    EnableReaction(A3)
    TracerDebugContinueInf
    goto(decision)

A3:
    TracerRegisterReactions(self+0x352bc,zero_wait,0x0)

    TracerRegisterReactions(self+0x352aa,change_to_suspended,0x0)
    AutorepeatReaction(change_to_suspended)

#    TracerRegisterReactions(self+0x3dc1d,A4,0x0)
#    TracerRegisterReactions(self+0x54cee,ST,0x0)
    TracerRegisterReactions(self+0x3d5fd,ST,0x0)

#    EnableReaction(A4)

    TracerDebugContinueInf
    goto(decision)
    
A4:
    TracerRegisterReactions(self+0x521b6,zero_wait_2,0x0)
    AutorepeatReaction(zero_wait_2)

    TracerRegisterReactions(self+0x521c5,flip3,0x105)
    AutorepeatReaction(flip3)

    TracerRegisterReactions(self+0x54cee,A5,0x0)
    EnableReaction(A5)

    TracerDebugContinueInf
    goto(decision)


A5:
    #TracerRegisterReactions(self+0x54d72,flip4,0x105)
    #AutorepeatReaction(flip4)

    TracerRegisterReactions(self+0x475ee,read_from_reg_1_flip_1,0x102)
    AutorepeatReaction(read_from_reg_1_flip_1)

    #TracerRegisterReactions(self+0x5280c,flip2,0x105)

    TracerRegisterReactions(self+0x4ea08,flip5,0x108)
    AutorepeatReaction(flip5)

    TracerRegisterReactions(self+0x4ea03,flip6,0x102)
    AutorepeatReaction(flip6)

    TracerRegisterReactions(self+0x4e9f3,flip7,0x103)
    AutorepeatReaction(flip7)

    TracerRegisterReactions(self+0x54d56,flip8,0x103)
    AutorepeatReaction(flip8)

#    TracerRegisterReactions(self+0x5a530,set_priority,0x0)


    TracerRegisterReactions(self+0x4e150,get_key_1,0x0)
    TracerRegisterReactions(self+0x475e6,deploy_val_1,0x0)

    TracerRegisterReactions(self+0x475ec,read_from_reg_1_check_1,0x0)
    TracerRegisterReactions(self+0x47596,read_from_reg_1_check_2,0x0)

    TracerRegisterReactions(self+0x4e188,retrieve_data_1_check_1,0x0)
    TracerRegisterReactions(self+0x4e203,retrieve_data_1_check_2,0x0)

    TracerRegisterReactions(self+0x4e9f1,retrieve_data_2_check_1,0x0)
    TracerRegisterReactions(self+0x4e9ff,retrieve_data_2_check_2,0x0)

    TracerRegisterReactions(self+0x4ea01,retrieve_data_2_check_3,0x0)
    TracerRegisterReactions(self+0x4ea05,retrieve_data_2_check_4,0x0)
    TracerRegisterReactions(self+0x4ea0a,retrieve_data_2_check_5,0x0)
    TracerRegisterReactions(self+0x4ea0e,retrieve_data_2_check_not_5,0x0)
    TracerRegisterReactions(self+0x4ea17,retrieve_data_2_check_9,0x0)

    TracerRegisterReactions(self+0x54d5c,thread_1_v_2_check_1,0x0)
    TracerRegisterReactions(self+0x54d78,thread_1_v_2_check_2,0x0)
    
    #TracerRegisterReactions(self+0x521bc,ST,0x0)
    TracerRegisterReactions(self+0x44157,ST,0x0)

    TracerDebugContinueInf
    goto(decision)

Start:
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTrace
    TracerDebugContinueInf

decision:
Decision=(
    CreateProcessA_start:thread_distribution,
    CreateProcessW_start:CreateProcessX_start,
    CreateProcessW_end:CreateProcessX_end,
    CREATEREMOTETHREAD+:thread_distribution,
    OPENPROCESS+:thread_distribution,
    SETTHREADCONTEXT+:thread_distribution,
    RESUMETHREAD+:thread_distribution,
    ST:Start,
    A1:A1,
    A2:A2,
    A3:A3,
    A4:A4,
    A5:A5,
    change_to_suspended:change_to_suspended,
    set_priority:set_priority,
    RE:re,
    RX:finish,
    zero_wait:zero_wait,
    zero_wait_2:zero_wait_2,
    get_key_1:get_key_1,
    deploy_val_1:deploy_val_1,
    fix_wait:fix_wait,
    retrieve_data_2_check_3:retrieve_data_2_check_3,
    read_from_reg_1_check_1:read_from_reg_1_check_1,
    default:loop
)

CreateProcessX_start:
    Call(PauseNewProcess)

    TracerDebugContinueInf
    goto(decision)

CreateProcessX_end:
    Call(ExtractPIDAndTID)
    Call(AttachFileLog)

    TracerRegisterReactions(self+0x55c44,A1:A2,0x0)
    TracerRegisterReactions(self+0x56015,A2,0x0)

    ResumeAll
    TracerDebugContinueInf
    goto(decision)

thread_distribution:
    ReadStack
    TracerDebugContinueInf
    goto(decision)

loop:
    TracerDebugContinueInf
    goto(decision)

re:
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exit:
    TracerPrev
    TracerDebugContinueInf
    goto(decision)

Exception:
    Interrupt
finish:
    QemuQuit

