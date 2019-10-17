# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/als_text_1/short)
    MLSetModelDir(_LEARNING/_MODELS)

    MLSetInputGlob(*.txt)
    MLSaveSamples(model_002.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(model_002_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(model_002_final)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

