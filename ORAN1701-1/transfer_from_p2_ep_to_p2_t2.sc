# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(QemuStartDontConnect)
    QemuLoad(p2_ep)
    goto(p2_ep)


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
    TracerRegisterReactions(self+0x352bc,injecting_zero_wait,0x0)
    AutorepeatReaction(injecting_zero_wait)

    TracerRegisterReactions(self+0x352aa,change_to_suspended,0x0)
    AutorepeatReaction(change_to_suspended)

#    TracerRegisterReactions(self+0x3dc1d,A4,0x0)
#    TracerRegisterReactions(self+0x54cee,p2_t1_v2,0x0)
    TracerRegisterReactions(self+0x5a4e8,p2_t2,0x0)
#    TracerRegisterReactions(self+0x54cee,ST,0x0)


#    EnableReaction(A4)

    TracerDebugContinueInf
    goto(decision)

p2_t2:
    DisableReaction(p2_t2)
    QemuSave(p2_t2)
    goto(finish)
 
A4:
    TracerRegisterReactions(self+0x521b6,thread_1_zero_wait,0x0)
    AutorepeatReaction(thread_1_zero_wait)

    TracerRegisterReactions(self+0x521c5,thread_1_flip_after_wait,0x105)
    AutorepeatReaction(thread_1_flip_after_wait)

    TracerRegisterReactions(self+0x4e10a,A5,0x0)
    EnableReaction(A5)
    AutorepeatReaction(A5)

    TracerDebugContinueInf
    goto(decision)


A5:
    ReadStack(0x10)
    ReadArg(0)
    CheckEqual(0x0044e9ef)=(N:decision)
    ReadArg(6)
    CheckEqual(0x00454d54)=(N:decision)

    DisableReaction(A5)
    #TracerRegisterReactions(self+0x54d72,flip4,0x105)
    #AutorepeatReaction(flip4)

#    TracerRegisterReactions(self+0x475ee,read_from_reg_1_flip_1,0x102)
#    AutorepeatReaction(read_from_reg_1_flip_1)

    #TracerRegisterReactions(self+0x5280c,flip2,0x105)

#    TracerRegisterReactions(self+0x4ea08,flip5,0x108)
#    AutorepeatReaction(flip5)

#    TracerRegisterReactions(self+0x4ea03,flip6,0x102)
#    AutorepeatReaction(flip6)

#    TracerRegisterReactions(self+0x4e9f3,flip7,0x103)
#    AutorepeatReaction(flip7)

#    TracerRegisterReactions(self+0x54d56,flip8,0x103)
#    AutorepeatReaction(flip8)

#    TracerRegisterReactions(self+0x54d72,flip9,0x102)
#    AutorepeatReaction(flip9)

#    TracerRegisterReactions(self+0x5a530,set_priority,0x0)


    TracerRegisterReactions(self+0x4e150,get_key_1,0x0)
    AutorepeatReaction(get_key_1)    

    TracerRegisterReactions(self+0x475e6,deploy_val_1,0x0)
    AutorepeatReaction(deploy_val_1)    

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
    TracerRegisterReactions(self+0x54d6e,thread_1_v_2_check_2,0x0)
    TracerRegisterReactions(self+0x54d78,thread_1_v_2_check_3,0x0)
    
    TracerRegisterReactions(self+0x5f137,deploy_val_2,0x0)

    #TracerRegisterReactions(self+0x521bc,ST,0x0)
    #TracerRegisterReactions(self+0x44157,ST,0x0)
#    TracerRegisterReactions(self+0x54d54,ST,0x0)
#    TracerRegisterReactions(self+0x4e203,ST,0x0)
    TracerRegisterReactions(self+0x47596,A6,0x0)
    #AutorepeatReaction(A6)
#    goto(Start)

    TracerDebugContinueInf
    goto(decision)

A6:
    TracerRegisterReactions(self+0x44151,A7,0x0)
    #AutorepeatReaction(A7)

    TracerDebugContinueInf
    goto(decision)
    
A7:
    #TracerRegisterReactions(self+0x56d99,ST,0x0)
    TracerRegisterReactions(self+0x36c43,ST,0x0)
    #AutorepeatReaction(ST)

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
    A6:A6,
    A7:A7,
    p2_t2:p2_t2,
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

CreateProcessX_start:
    Call(PauseNewProcess)

    TracerDebugContinueInf
    goto(decision)

p2_ep:
    Call(QemuReconnect)
    Call(ReconfigureDirs)
    
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

