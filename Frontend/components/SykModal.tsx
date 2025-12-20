import {useState} from "react";
import {Modal, Pressable, StyleSheet, Text, TextInput, TouchableWithoutFeedback, View} from "react-native";

type SykModalProps = {
    isVisible: boolean;
    setIsVisible: (isVisible: boolean) => void;
    onSubmit: (symptoms: string) => void;
}

export default function SykModal({
                                     isVisible,
                                     setIsVisible,
                                     onSubmit,
                                 }: SykModalProps) {
    const [symptoms, setSymptoms] = useState("");

    return (
        <Modal transparent visible={isVisible} animationType={"fade"}>
            <TouchableWithoutFeedback onPress={() => setIsVisible(false)}>
                <View style={styles.overlay}>
                    <TouchableWithoutFeedback onPress={(e) => e.stopPropagation()}>
                        <View style={styles.modalVisible}>
                            <Text style={styles.title}>Sykdom</Text>
                            <Text style={styles.infoText}>
                                Ved sykdom er det viktig at vi logger tid og årsak ved eventuell smittsom sykdom,
                                vennligst beskriv symptomene og legg inn tidspunkt for når de oppstod/når det ble
                                meldt av forelder.
                            </Text>
                            <Text style={styles.subTitle}>Symptomer:</Text>
                            <View style={styles.inputBoxContainer}>
                                <TextInput
                                    value={symptoms}
                                    onChangeText={setSymptoms}
                                    style={styles.inputBox}
                                />
                            </View>
                            <Pressable
                                style={styles.addButton}
                                onPress={() => {
                                    onSubmit(symptoms);
                                    setSymptoms("");
                                    setIsVisible(false);
                                }}
                            >
                                <Text style={styles.addButtonText}>Lagre sykdom</Text>
                            </Pressable>
                        </View>
                    </TouchableWithoutFeedback>
                </View>
            </TouchableWithoutFeedback>
        </Modal>
    )
}

const styles = StyleSheet.create({
    overlay: {
        flex: 1,
        backgroundColor: "#00000044",
        justifyContent: "center",
        alignItems: "center",
    },
    modalVisible: {
        width: 300,
        padding: 20,
        borderRadius: 8,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "white",
    },
    title: {
        fontSize: 24,
        fontWeight: "600",
    },
    infoText: {
        color: "#6c6868",
        fontFamily: "Inter",
        fontSize: 14,
        fontWeight: "400",
        letterSpacing: 0.28,
        paddingVertical: 16,
    },
    subTitle: {
        fontSize: 12,
        fontFamily: "Inter",
        fontStyle: "normal",
        fontWeight: "400",
        textTransform: "uppercase",
    },
    inputBoxContainer: {
        paddingBottom: 12,
    },
    inputBox: {
        width: 224,
        height: 50,
        backgroundColor: "#EFF4FF",
        borderRadius: 10,
        paddingVertical: 12,
        paddingHorizontal: 15,
        gap: 10,
    },
    addButtonText: {
        fontSize: 16,
        fontFamily: "Inter",
        fontWeight: "400",
        color: "#ffffff",
    },
    addButton: {
        width: 168,
        borderRadius: 40,
        justifyContent: "center",
        alignItems: "center",
        gap: 10,
        paddingVertical: 12,
        backgroundColor: "#011638",
    }
})