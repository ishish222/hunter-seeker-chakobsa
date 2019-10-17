# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_MALWARE_SAMPLES/_WORKING/BACKSWAP_2019/*)
    HostDeployInputGlob
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    Call(QemuStartRevert)
#    QemuStart
#    QemuLoad(clean)

    RunCommand(copy \\10.0.2.4\qemu\input\* c:\users\john\desktop\)
    SetInDir(c:\users\john\desktop)
    SetSampleFile(backswap_0612.bin.exe)

    Call(DebugFileLog)
    Call(RegisterEnableBuiltin)
    DisableReactions

#    ExtractEP(c:\users\john\desktop\backswap_0612.bin.exe)
#    SaveEP
#    ManualSTwSelf
#    RegisterReactions(0x51cc8d,ST,0x0)
    RegisterReactions(self+0x11d56d,step_1,0x0)
    Continue=

step_1:
    ReadRegister(EAX)
    Push
    SetBase(decoded)
    RegisterReactions(decoded+0x584,step_2,0x0)
    Continue=

step_2:
    RegisterReactions(decoded+0x58f,step_3,0x0)
    Continue=

step_3:
    RegisterReactions(decoded+0xa4b4,step_4,0x0)
    Continue=

step_4:
    RegisterReactions(decoded+0xf5b6,sleep_1,0x0)
    RegisterReactions(decoded+0xf2d1,winhook,0x0)
    #RegisterReactions(decoded+0xa506,ST,0x0)
    Continue=

sleep_1:
    ReadStack
    ReadRegister(ESP)
    Push
    WriteDword(0x0)
    Continue=

winhook:
    ReadStack
    ReadRegister(ESP)
    Adjust(0xc)
    Push
    ReadDword
    Push
    RegisterReactionsAt(ST,0x0)
    Continue=

ST:
    DisableReaction(ST)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    TracerStartTraceLight
    Continue=

Default:
    Continue=

RE:
    Pause
    Continue=

EX:
    Pause
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

