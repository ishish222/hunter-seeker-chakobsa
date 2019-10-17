# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    #GlobPattern(_FUZZING_SAMPLES/ableton/crashes/binned_01/0x011e5423/*)
    GlobPattern(_FUZZING_SAMPLES/ableton/crashes/binned_01/0x011f1dc3/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    DumpPhysicalMemory
    Pause
    QemuQuit

Default:
    Continue=

Exception:
finish:
    QemuQuit

