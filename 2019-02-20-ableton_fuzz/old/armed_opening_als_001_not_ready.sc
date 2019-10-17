# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_SAMPLES/_FUZZING_SAMPLES/ableton/fuzz_ableton/originals/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(armed)
    Call(ReconfigureDirs)

    Main_Copy:
        RunCommand(copy \\10.0.2.4\qemu\input\sample.als c:\users\john\desktop\sample.als)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA,0x0;kernel32.dll+0x00050b5d,CreateFileW,0x0)
    AutorepeatReaction(CreateFileA)
    AutorepeatReaction(CreateFileW)

    RunCommand(start c:\users\john\desktop\sample.als)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)


CreateFileA:
    ReadArgAnsi(0x1)    
    CheckStrStr(Sample_Reference)=(Y:StartAnalysis,N:DontStartAnalysis)
    
CreateFileW:
    ReadArgUni(0x1)    
    CheckStrStr(Sample_Reference)=(Y:StartAnalysis,N:DontStartAnalysis)

StartAnalysis:
    DisableReaction(CreateFileA)
    DisableReaction(CreateFileW)

    DumpMemory
    SecureAllSections
    TracerStartTrace
    Continue=

DontStartAnalysis:
    Continue=

RE:
    Continue(0x80010001)=

Default: 
finish:
    QemuQuit

