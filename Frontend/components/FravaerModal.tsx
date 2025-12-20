import {useState} from "react";
import {Modal, Pressable, StyleSheet, Text, TextInput, TouchableWithoutFeedback, View} from "react-native";

type FravaerModalProps = {
    isVisible: boolean;
    setIsVisible: (isVisible: boolean) => void;
    onSubmit: (reasons: string) => void;
}

export default function FravaerModal({
    isVisible,
    setIsVisible,
    onSubmit,
    }: FravaerModalProps) {
    const [reason, setReason] = useState("");

    return (
        <Modal transparent visible={isVisible} animationType={"fade"}>
            <TouchableWithoutFeedback onPress={() => setIsVisible(false)}>
                <View style={styles.overlay}>
                    <TouchableWithoutFeedback onPress={(e) => e.stopPropagation()}>
                        <View style={styles.modalVisible}>
                            <Text style={styles.title}>Fravær</Text>
                            <Text style={styles.infoText}>
                                Beskriv årsaken til fravær og legg inn en beskjed hvis nødvendig,
                                dette er så avdelingsleder kan følge opp saken senere.
                            </Text>
                            <Text style={styles.subTitle}>Årsak for fravær:</Text>
                            <View style={styles.inputBoxContainer}>
                                <TextInput
                                    value={reason}
                                    onChangeText={setReason}
                                    style={styles.inputBox}
                                />
                            </View>
                            <Pressable
                                style={styles.addButton}
                                onPress={() => {
                                    onSubmit(reason);
                                    setReason("");
                                    setIsVisible(false);
                                }}
                            >
                                <Text style={styles.addButtonText}>Lagre fravær</Text>
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
    },
})