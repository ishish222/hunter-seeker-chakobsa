# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
#    GlobPattern(_FUZZING_SAMPLES/xara/binned_01/0x00702d56/*.xar)
    GlobPattern(_FUZZING_SAMPLES/xara/originals_01/*.xar)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    GetPIDByMatch(Designer)
    Push
    SetPID

    Call(AttachFileLog)

    Main_Copy:
        RunCommand(copy \\10.0.2.4\qemu\input\a6fc6e3ca34c4b6918ef780e5afb5156.xar c:\users\john\desktop\sample.xar)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA_start,0x0;kernel32.dll+0x52942,CreateFileA_end,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,CreateFileW_start,0x0;kernel32.dll+0x50ba8,CreateFileW_end,0x0)
    RegisterReactions(self+0xae7495,GotLoaded,0x0)
    EnableReaction(GotLoaded)

    EnableReaction(CreateFileA_start)
    EnableReaction(CreateFileW_start)
    DisableReaction(CreateFileA_end)
    DisableReaction(CreateFileW_end)
    AutorepeatReaction(CreateFileA_start)
    AutorepeatReaction(CreateFileW_start)

    RunCommand(start c:\users\john\desktop\sample.xar)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

CreateFileA_start:
    ReadArgAnsi(0x1)
    CheckStrStr(sample)=(N:CreateFileA_start_skip)
    EnableReaction(CreateFileA_end)
    CompareCounter(1)=(Y:CreateFileA_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    
    #ReadFile, need to be separate in order not to fire off each other
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    DisableReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileA_end)

    CreateFileA_start_skip:

    Continue=

CreateFileA_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=
   
CreateFileW_start:
    ReadArgUni(0x1)
    CheckStrStr(sample)=(N:CreateFileW_start_skip)
    EnableReaction(CreateFileW_end)
    CompareCounter(1)=(Y:CreateFileW_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    
    #ReadFile
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    DisableReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileW_end)

    CreateFileW_start_skip:

    Continue=

CreateFileW_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=

ReadFile_start:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Str
    Push
    CheckStrStrWQueue=(Y:Interesting)
    Continue=
    
Interesting: 
    ReadStack

    ReadRegister(ESP)
    Adjust(0xc)
    Push
    ReadDword
    Push

    ReadRegister(ESP)
    Adjust(0x10)
    Push
    ReadDword
    Push

    RegisterRegions
    EnableReaction(ReadFile_end)
    Continue=

ReadFile_end:
    TaintLastRegion
    DisableReaction(ReadFile_end)
    Continue=

StartAnalysis:
    DumpMemory
    #SecureAllSections
    #AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    ResizeOutBuffer(0x10000000)
    ResizeModBuffer(0x1000000)
    ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    #TracerStartTraceLight
    TracerStartTraceDebug
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xe06d7363)=(Y:RE_continue)

GotLoaded:
RE_crash:
    FlushFiles
    Pause

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

Exception:
finish:
    QemuQuit

