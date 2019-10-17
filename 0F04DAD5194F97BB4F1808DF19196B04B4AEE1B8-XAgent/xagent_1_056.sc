# exploring mailslot functionality - spawning mailslot_client, trying to send

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(netapi64.dll)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/netapi64.dll)
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
SetSampleFile(rundll32.exe)
SetResearchDir(c:\windows\system32)
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
TracerSetParameters(e:\samples\shared\netapi64.dll,init)
TracerDebugSample
TracerDebugContinueInf

# RR
TracerRegisterReactions(self+0x1867,print1,0x360)
TracerRegisterReactions(self+0x1877,flip1,0x105)
TracerRegisterReactions(self+0x18d8,flip2,0x105)
TracerRegisterReactions(netapi64.dll+0xb6c3,ST,0x0)
DisableReactions
EnableReaction(ST)
TracerRegisterReactions(netapi64.dll+0xb45f,spawn_mailslot,0x0)
EnableReaction(spawn_mailslot)
AutorepeatReaction(spawn_mailslot)

TracerDebugContinueInf
goto(decision)


ST:
    DumpMemory
    SecureAllSections
    #EnableReaction(flip1)
    #EnableReaction(flip2)
    EnableReaction(print1)
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

# modifications
    TracerRegisterReactions(
        SHELL32.dll+0x0141f2,SHELLEXEC+2:SHELLEXEC-1,0x0;
        SHELL32.dll+0x014276,SHELLEXEC-1:SHELLEXEC+2,0x100;
        )
    EnableReaction(SHELLEXEC+2)

    Execute(scripts/arab/enable_context_mod_detection.sc)

    # we dont need for now, we pass by first creation
    TracerStartTrace
    TracerDebugContinueInf
    goto(decision)

decision:
Decision=(RE:re,
    ST:ST,
    SHELLEXEC+2:inspect_shellexecute,
    spawn_mailslot:spawn_mailslot,
    default:loop)

name:
    ReadRegister(EAX)
    ReadUnicode
#    SpawnMailslot
    TracerDebugContinueInf
    goto(decision)

spawn_mailslot:
    SpawnMailslot(\\.\mailslot\95ca3a2a-6503-49da-9fe4-21bce3e6dc8)
    MailslotNextResponse(SCREENTestAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa)
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
RunCmdHost(mkdir -p /mnt/1/output/logs/xagent_1_056)
RunCmd(copy e:\server\log_0.txt \\10.0.2.4\qemu\logs\xagent_1_056)
RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\xagent_1_056)
QemuQuit
