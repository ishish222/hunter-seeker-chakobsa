# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(june.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/june/june.exe)
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
#ExtractEP(e:\samples\shared\june.exe) 
#SaveEP 
#ManualSTwSelf 
DisableReactions
#EnableReaction(ST)

TracerRegisterReactions(self+0x1d7db,mod1:mod2,0x100;edi-0x62,mod2:mod3,0x100;ecx+0x586,mod3:ST:aaaa,0x100;ecx+0x21b7,ST,0x0;ecx+0x21c0,aaaa,0x0)
EnableReaction(mod1)
DisableReaction(mod2)
DisableReaction(mod3)
DisableReaction(ST)
DisableReaction(aaaa)

TracerDebugContinueInf
goto(decision)




Start:
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

    # we dont need for now, we pass by first creation
    TracerStartTrace
    TracerDebugContinueInf

decision:
Decision=(
    ST:Start,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    aaaa:aaaa,
    default:loop
)

aaaa:
    ReadStack
    ReadRegister(ESP)
    WriteDword(0x0)
    ReadStack
    TracerDebugContinueInf
    goto(decision)


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
    RunCmdHost(mkdir -p /mnt/1/output/logs/june_002)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\june_002)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\june_002)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\june_002)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\june_002)
    QemuQuit

