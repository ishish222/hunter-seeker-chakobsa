# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_MALWARE_SAMPLES/_WORKING/mr_code/*)
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
    SetSampleFile(mrcode.exe)

    SpawnTracerFileLog
    TracerConfigureSample
    TracerSetParameters(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerDebugSample

    RegisterReactions(self+0x1569,ST,0x0)

    Continue=

ST:
    RegisterReactions(ntdll.dll+0x000464f0,KiFastSystemCall_start:KiFastSystemCallRet_start,0x1)
    RegisterReactions(ntdll.dll+0x000464f4,KiFastSystemCallRet_start:KiFastSystemCall_start,0x2)
    EnableReaction(KiFastSystemCall_start)
    DisableReaction(KiFastSystemCallRet_start)
    RaiseReaction(KiFastSystemCall_start)
    RaiseReaction(KiFastSystemCallRet_start)

    DisableReaction(ST)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    TracerStartTraceDebug
    TracerDebugContinueInf
    Pause

    Continue=

Default:
    Continue=

EXCEPTION:
EX:
    Continue(0x80010001)=

Exception:
RE:
RX:
    Interrupt
    HostPrint(Finishing)

finish:
    QemuQuit

