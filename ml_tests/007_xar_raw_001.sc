# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/xar_raw_001)
    MLSetModelDir(_LEARNING/_MODELS)

    MLSetInputGlob(*)
    MLSaveSamples(xar_raw_001.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(xar_raw_001_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(xar_raw_001)
    MLRemoveModel(xar_raw_001_part)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

