# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(sample_869d.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/ORAN1701-1/*)
SetOutDir(\\10.0.2.4\qemu)
CheckHostDir
RevertClean
EnableLogging
PrepareStats
PreparePipes
GlobMethod
DiskGlob
StartQemuFull
QemuMountDisks

start_controller:
SpawnInternalController
QemuConnectDevSocket
IsSocketConnected=(Y:success,N:fail)

fail:
Wait10
goto(start_controller)

success:
KillExplorer
ResetTracers
SpawnTracerController
SpawnTracerFileLog
TracerConfigureSample
TracerConfigureOutDir
TracerConfigureOutPrefix
TracerConfigureInDir
TracerPrepareTrace

TracerRegisterBuiltin
DisableReactions
TracerDebugSample
TracerDebugContinueInf

# RR
ExtractEP(e:\samples\shared\sample_869d.exe) 
#SaveEP 
#ManualSTwSelf 
DisableReactions
#TracerRegisterReactions(self+0x55c44,A1:A2,0x0)
#TracerRegisterReactions(self+0x56015,A2,0x0)
#EnableReaction(A2)
Execute(scripts/common/enable_thread_tracking.sc)
TracerDebugContinueInf
goto(decision)

change_to_suspended:
    ReadRegister(ESP)
    Adjust(0x14)
    Push
    WriteDword(0x4)
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

    Wait(10)
    TracerDebugContinueInf
    goto(decision)

check2:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

check1:
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

    TracerRegisterReactions(self+0x3dc1d,A4,0x0)
    EnableReaction(A4)

    TracerDebugContinueInf
    goto(decision)
    
A4:
    TracerRegisterReactions(self+0x521b6,zero_wait_2,0x0)
    AutorepeatReaction(zero_wait_2)

    TracerRegisterReactions(self+0x521c5,flip3,0x105)
    TracerRegisterReactions(self+0x54cee,A5,0x0)
    EnableReaction(A5)

    TracerDebugContinueInf
    goto(decision)


A5:
    #TracerRegisterReactions(self+0x54d72,flip4,0x105)
    TracerRegisterReactions(self+0x4ea08,flip5,0x108)
    TracerRegisterReactions(self+0x4ea03,flip6,0x102)
    TracerRegisterReactions(self+0x4e9f3,flip7,0x103)
    TracerRegisterReactions(self+0x54d56,flip8,0x103)
    AutorepeatReaction(flip3)
    #AutorepeatReaction(flip4)
    AutorepeatReaction(flip5)
    AutorepeatReaction(flip6)
    AutorepeatReaction(flip7)
    AutorepeatReaction(flip8)

#    TracerRegisterReactions(self+0x5a530,set_priority,0x0)


    #TracerRegisterReactions(self+0x5280c,flip2,0x105)
    #TracerRegisterReactions(self+0x521bc,ST,0x0)
    TracerRegisterReactions(self+0x44308,ST,0x0)
    TracerRegisterReactions(self+0x4e150,get_key_1,0x0)
    TracerRegisterReactions(self+0x475e6,deploy_val_1,0x0)

    TracerRegisterReactions(self+0x4ea01,check1,0x0)
    TracerRegisterReactions(self+0x4e9ff,check2,0x0)
    TracerRegisterReactions(self+0x4ea0a,check3,0x0)
    TracerRegisterReactions(self+0x4ea0e,check_not_3,0x0)
    TracerRegisterReactions(self+0x54d78,check4,0x0)
    TracerRegisterReactions(self+0x4ea05,check5,0x0)
    TracerRegisterReactions(self+0x475ec,check6,0x0)
    TracerRegisterReactions(self+0x47596,check7,0x0)
    TracerRegisterReactions(self+0x4ea05,check8,0x0)
    TracerRegisterReactions(self+0x4e9f1,check9,0x0)
    TracerRegisterReactions(self+0x54d5c,check10,0x0)
    TracerDebugContinueInf
    goto(decision)

Start:
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

    TracerStartTrace
    TracerDebugContinueInf

decision:
Decision=(
    CREATEPROCESSA+:thread_distribution,
    CREATEPROCESSW+:CREATEPROCESS+,
    CREATEPROCESSW-:CREATEPROCESS-,
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
    check1:check1,
    check2:check2,
    default:loop
)

CREATEPROCESS+:
    Execute(scripts/common/pause_new_process.sc)
    TracerDebugContinueInf
    goto(decision)

CREATEPROCESS-:
    ReadRegister(ESP)
    Adjust(0x2c)
    Push
    ReadDword
    Adjust(0x8)
    Push
    ReadPID

    ReadRegister(ESP)
    Adjust(0x2c)
    Push
    ReadDword
    Adjust(0xc)
    Push
    ReadTID

    goto(attach_resume)

attach_resume:
    SpawnTracerLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerRegisterBuiltin
    DisableReactions
    TracerAttachSample

    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

#    Execute(scripts/common/enable_interesting_api.sc)

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

exception:
    Interrupt
finish:
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_3)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    QemuQuit

