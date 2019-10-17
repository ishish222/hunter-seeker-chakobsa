# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    GlobPattern(fuzz_ableton)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    SetInDir(C:\ProgramData\Ableton\Live 9 Trial\Program)
    SetSampleFile(Ableton Live 9 Trial.exe)

    Call(QemuStart)

    Spawn(C:\ProgramData\Ableton\Live 9 Trial\Program\Ableton Live 9 Trial.exe)
    Pause

    GetPIDByMatch(Ableton Live 9 Trial.exe)
    Push
    SetPID

    Call(AttachFileLog)

    #RegisterReactions(self+0xe93211,ST,0x0)
    RegisterReactions(self+0x1be41c3,Entered,0x0)
    EnableReaction(Prepare)
    Continue=

Entered:
    # verify
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID
    Push
    SuspendAllExcept


    RegisterReactions(self+0x1be42ce,ST1,0x0)
    RegisterReactions(self+0x1be4288,ST2,0x0)
    RegisterReactions(self+0x1be42fd,ST3,0x0)
    RegisterReactions(self+0x1be42eb,ST4,0x0)
    RegisterReactions(self+0x1be431f,ST5,0x0)
    RegisterReactions(self+0x1be433b,ST6,0x0)
    RegisterReactions(self+0x1be42b9,ST7,0x0)
    RegisterReactions(self+0xdabfe8,finish,0x0)
    EnableReaction(ST1)
    EnableReaction(ST2)
    EnableReaction(ST3)
    EnableReaction(ST4)
    EnableReaction(ST5)
    EnableReaction(ST6)
    EnableReaction(ST7)
    EnableReaction(finish)

    Continue=


ST1:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST2:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST3:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST4:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST5:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST6:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

ST7:
    ReadStack(0x10)
    ReadArg(0xb)
    CheckEqual(0x11abfe8)=(N:Default)
    CurrentTID

    ReadStack(0x10)
    DisableReaction(ST1)
    DisableReaction(ST2)
    DisableReaction(ST3)
    DisableReaction(ST4)
    DisableReaction(ST5)
    DisableReaction(ST6)
    DisableReaction(ST7)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

Default:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt
finish:
    QemuQuit

