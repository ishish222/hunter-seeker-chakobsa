Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/ableton/fuzzing_samples/*)
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

    RegisterReactions(self+0xe4b130,ST,0x0)
    RegisterReactions(self+0x169a9db,EN,0x0)
    EnableReaction(ST)
    DisableReaction(EN)

    Copying:
        RunCommand(copy \\10.0.2.4\qemu\input\tmpPNYgK_.amxd c:\users\john\desktop\tmpPNYgK_.amxd)
        CheckStrStr(cannot)=(Y:Sleep_a_little)

    RunCommand(start c:\users\john\desktop\tmpPNYgK_.amxd)

    Continue=

    Sleep_a_little:
        Wait(1)
        goto(Copying)
        
ST:
    DumpMemory
    SecureAllSections
    TracerStartTraceLight

    EnableReaction(EN)
    Continue=

EN:
    TracerPrintResult([EN])
    goto(finish)

RE:
    Continue(0x80010001)=

Default: 
finish:
    QemuQuit

