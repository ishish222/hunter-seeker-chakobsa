# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/als_text_compressed_2/split)
    MLSetModelDir(_LEARNING/_MODELS)

    MLSetInputGlob(*)
    MLSaveSamples(model_006_compressed.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(model_006_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(model_006_compressed)
    MLRemoveModel(model_006_part)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

