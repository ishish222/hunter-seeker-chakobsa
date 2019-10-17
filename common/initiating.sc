Intro:
#    PrintLogo
    SetInDir(\\10.0.2.4\qemu\input)
    SetOutDir(\\10.0.2.4\qemu\output)
    CheckHostDir
    ResetTime
    Return

QemuReconnect:
    QemuReconnect_start_controller:
        QemuConnectDevSocket
        IsSocketConnected=(Y:QemuReconnect_success,N:QemuReconnect_fail)

    QemuReconnect_fail:
        Wait(10)
        goto(QemuReconnect_start_controller)

    QemuReconnect_success:
    TracerReopenIO
    Return

QemuStartDontConnect:
    QemuStart
    Return

QemuStartRevert:
    OfflineRevertClean
    QemuStart
    RevertReady

    QemuStartRevert_start_controller:
        SpawnInternalControllerSmb
        QemuConnectDevSocket
        IsSocketConnected=(Y:QemuStartRevert_success,N:QemuStartRevert_fail)

    QemuStartRevert_fail:
        Wait(10)
        goto(QemuStartRevert_start_controller)

    QemuStartRevert_success:
        KillExplorer
        HostResetTracers
        SpawnTracerController

    Return

QemuStart:
    OfflineRevertClean
    QemuStart

    QemuStart_start_controller:
        SpawnInternalControllerSmb
        QemuConnectDevSocket
        IsSocketConnected=(Y:QemuStart_success,N:QemuStart_fail)

    QemuStart_fail:
        Wait(10)
        goto(QemuStart_start_controller)

    QemuStart_success:
        KillExplorer
        HostResetTracers
        SpawnTracerController

    Return

QemuStartFileLog:
    OfflineRevertClean
    QemuStart

    QemuStartFileLog_start_controller:
        SpawnInternalControllerFileLogSmb
        QemuConnectDevSocket
        IsSocketConnected=(Y:QemuStartFileLog_success,N:QemuStartFileLog_fail)

    QemuStartFileLog_fail:
        Wait(10)
        goto(QemuStartFileLog_start_controller)

    QemuStartFileLog_success:
        KillExplorer
        HostResetTracers
        SpawnTracerController

    Return
