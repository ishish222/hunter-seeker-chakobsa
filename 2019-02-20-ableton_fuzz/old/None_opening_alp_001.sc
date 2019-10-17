# initial record
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

    Call(QemuStartRevert)

    SetInDir(C:\ProgramData\Ableton\Live 9 Trial\Program)
    SetSampleFile(Ableton Live 9 Trial.exe)

    # configure tracing parameters and start the process (modified from lib)
    SpawnTracerFileLog
    TracerConfigureSample
    TracerSetParameters(\\10.0.2.4\qemu\input\sample.alp)
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerDebugSample

    # extract EP and BP
    ExtractEP(C:\ProgramData\Ableton\Live 9 Trial\Program\Ableton Live 9 Trial.exe)
    SaveEP
    ManualSTwSelf

    Continue=

RR:
    Continue=

ST:
    GetCurrentConfig
    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA,0x0;kernel32.dll+0x00050b5d,CreateFileW,0x0)
    EnableReaction(CreateFileA)
    AutorepeatReaction(CreateFileA)
    EnableReaction(CreateFileW)
    AutorepeatReaction(CreateFileW)

    Continue=

CreateFileA:
    ReadArgAnsi(0x1)    
    CheckStrStr(sample)=(Y:StartAnalysis,N:DontStartAnalysis)
    
CreateFileW:
    ReadArgUni(0x1)    
    CheckStrStr(sample)=(Y:StartAnalysis,N:DontStartAnalysis)

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

