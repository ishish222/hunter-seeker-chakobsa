# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/als_text_compressed_1/short)
    MLSetModelDir(_LEARNING/_MODELS)

    MLSetInputGlob(*.short)
    MLSaveSamples(model_004.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(model_004_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(model_004_compressed)
    MLRemoveModel(model_004_part)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

