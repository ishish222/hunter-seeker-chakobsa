# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(QemuStartDontConnect)
    QemuLoad(p2_t1_v2_b)
    goto(p2_t1_v2_b)

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
    ReadStack
    ReadArgUni(0x1)
    Enqueue

    TracerDebugContinueInf
    goto(decision)


deploy_val_2:
    DisableReaction(deploy_val_2)
    ReadRegister(EAX)
    Push

    Enqueue(bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb)

    ReadUnicode
    Enqueue
    

    Str(echo )
    StrCatQueue
    StrCat( > )
    StrCatQueue

    Push
    RunCommand

    Wait(10)
    TracerDebugContinueInf
    goto(decision)

deploy_val_1:
    ReadStack
    ReadArg(0xb)
    CheckEqual(0x0044e173)=(N:deploy_val_1_nope)


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

    #ListTebs
    CurrentTID
    Push
    SuspendAllExcept
    #ListTebs

    Wait(10)

    deploy_val_1_nope:

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
    Adjust(0x0)
    Push
    WriteDword(0x0)

    ReadRegister(ESP)
    Adjust(0x4)
    Push
    WriteDword(0x0)

    TracerDebugContinueInf
    goto(decision)

fix_wait:
    Push(0x1)
    WriteRegister(EAX) 
    TracerDebugContinueInf
    goto(decision)

retrieve_data_2_check_5:
    Push(0x94)
    WriteRegister(EAX) 

    TracerDebugContinueInf
    goto(decision)

p2_t1_v2_b:
    Call(QemuReconnect)
    Call(ReconfigureDirs)

    TracerAppendPIDPrefix
    TracerPrepareTrace

    ResumeAll
    TracerRegisterReactions(self+0x44308,ST,0x0)

    TracerDebugContinueInf
    goto(decision)
    

Start:
    DisableReaction(ST)

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
    ST:Start,
    p2_t1_v2_b:p2_t1_v2_b,
    change_to_suspended:change_to_suspended,
    set_priority:set_priority,
    RE:re,
    EXCEPTION:re,
    RX:finish,
    thread_1_zero_wait:zero_wait,
    injecting_zero_wait:zero_wait,
    get_key_1:get_key_1,
    deploy_val_1:deploy_val_1,
    deploy_val_2:deploy_val_2,
    fix_wait:fix_wait,
    retrieve_data_2_check_3:retrieve_data_2_check_3,
    read_from_reg_1_check_1:read_from_reg_1_check_1,
    retrieve_data_2_check_5:retrieve_data_2_check_5,
    default:loop
)

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

