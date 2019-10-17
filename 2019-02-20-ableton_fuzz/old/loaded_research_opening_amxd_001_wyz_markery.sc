Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(flareon_samples/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    GetPIDByMatch(Trial)
    Push
    SetPID

    Call(AttachScrLog)

    RegisterReactions(self+0x216bc1,ST,0x0)
    RegisterReactions(self+0x2169e0,EN,0x0)
    EnableReaction(ST)
    DisableReaction(EN)

    Copying:
        RunCommand(copy \\10.0.2.4\qemu\input\sample_1.alp c:\users\john\desktop\sample_1.amxd)
        CheckStrStr(cannot)=(Y:Sleep_a_little)

    RunCommand(start c:\users\john\desktop\sample_1.amxd)

    Continue=

    Sleep_a_little:
        Wait(1)
        goto(Copying)
        
ST:
    DumpMemory
    SecureAllSections
    TracerStartTrace

    EnableReaction(EN)
    Continue=

EN:
    TracerPrintResult([EN])
    Continue=

RE:
    Continue(0x80010001)=

Default: 
finish:
    QemuQuit

