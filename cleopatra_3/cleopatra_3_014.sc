# what if mutex wxists
# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(22277.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/2017-08-09-august/9034/22277.exe)
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
#ExtractEP(e:\samples\shared\22277.exe) 
#SaveEP 
#ManualSTwSelf 
TracerRegisterReactions(self+0x13ec,A1,0x0)

DisableReactions
EnableReaction(A1)
TracerDebugContinueInf
goto(decision)


Start:
    #DumpMemory
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
    Execute(scripts/common/enable_context_mod_detection.sc)

    # we dont need for now, we pass by first creation
    #TracerStartTrace
    # hook proces creation
    EnableReaction(CREATEPROCESSA-)
    EnableReaction(CREATEPROCESSW-)

    TracerDebugContinueInf



decision:
Decision=(
    ST:Start,
    A1:A1,
    CREATEPROCESSA-:CREATEPROCESS,
    CREATEPROCESSW-:CREATEPROCESS,
    SETTHREADCONTEXT+:SETTHREADCONTEXT,
    RESUMETHREAD+:RESUMETHREAD,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    default:loop
)

CREATEPROCESS:
    ReadRegister(ESP)
    Adjust(0x2c)
    ReadDword
    Adjust(0x8)
    ReadPID
    ReadRegister(ESP)
    Adjust(0x2c)
    ReadDword
    Adjust(0xc)
    ReadTID
    EnableReaction(SETTHREADCONTEXT+)
    TracerDebugContinueInf
    goto(decision)

SETTHREADCONTEXT:
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    # extract EIP
    #EAX
    Adjust(0xb0)
    ReadDword
    SaveEP
    EnableReaction(RESUMETHREAD+)
    TracerDebugContinueInf
    goto(decision)

RESUMETHREAD:
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

    #RR
    LoadEP
    #ManualST
    TracerRegisterReactions(0x407126,flip2,0x108)
    TracerRegisterReactions(0x4079dc,ST2,0x0)
    TracerRegisterReactions(0x407132,select,0x0)
    TracerRegisterReactions(0x401634,flip1,0x105)
    ResumeThread
    # values for writing in EAX
    SetCounter(0x2)
    Push(0x1)
    Push(0x1)
    Push(0x2)
    TracerDebugContinueInf

decision2:
Decision=(
    ST2:Start2,
    select:select,
    CREATEPROCESSA-:CREATEPROCESS,
    CREATEPROCESSW-:CREATEPROCESS,
    SETTHREADCONTEXT+:SETTHREADCONTEXT,
    RESUMETHREAD+:RESUMETHREAD,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    default:loop
)

select:
    CheckCounter=(Y:disable_select,N:ignore_select)
disable_select:
    DisableReaction(select)
ignore_select:
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision2)



Start2:
    DisableReaction(select)
    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
    Execute(scripts/common/enable_context_mod_detection.sc)

    TracerStartTrace
    TracerDebugContinueInf
    goto(decision2)

check_size_read_3:
    ReadRegister(EBX)
check_send_req:
check_size_read_1:
check_size_read_2:
check_size_read_4:
check_buffer:
check_int_read:
    ReadRegister(EAX)
    #Pause
    TracerDebugContinueInf
    goto(decision)

A1:
    ResolveLocation(self+0xf4a4)
    ReadDword
    SaveEP
    ManualST
    EnableReaction(ST)
    TracerDebugContinueInf
    goto(decision)

read_arg_1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x0)
    ReadDword
    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_1:
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

read_arg_3:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_3:
    Pop
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

update_content_len:
    Push(0xd8)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

httpsend:
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

disable_ssl:
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    CheckEqual(0x1f)=(Y:disable,N:ignore)

disable:
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    WriteDword(0x0)

ignore:
    TracerDebugContinueInf
    goto(decision)

disable2:
    ReadRegister(ESP)
    Adjust(0x1c)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

get_info1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0x10)
    ReadDword
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info2:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

get_info3:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info4:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

overwrite_address:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    WriteUnicode(127.0.0.1)

    ReadRegister(ESP)
    Adjust(0xc)
    #9999
    WriteDword(0x270f)
    #and now port
    ReadStack

    TracerDebugContinueInf
    goto(decision)

internetopen:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)


inspect_shellexecute:
    DumpFile(C:\ProgramData\netapi64.dll)
    Beep
    TracerDebugContinueInf
    goto(decision)

overwrite:
    ReadRegister(ESP)
    Adjust(0x4)
    WriteDword(0x1)
    TracerDebugContinueInf
    goto(decision)

overwrite2:
    ReadRegister(ESP)
    Adjust(0x0)
    WriteDword(0xa)
    TracerDebugContinueInf
    goto(decision)

loop:
    TracerDebugContinueInf
    goto(decision)

re:
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exception:
    Interrupt
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_3)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    QemuQuit

