import {FilterStatus} from "@/types/child";
import React, {useRef, useState} from "react";
import {Modal, StyleSheet, Text, TouchableOpacity, View} from "react-native";
import { statusStyles } from "@/components/ui/statusStyles";

const DropDownMenuCrosslist = ({selected, onSelect, options} : {
    selected: string;
    onSelect: (value: FilterStatus) => void;
    options: string[];
}) => {
    const [menuOpen, setMenuOpen] = useState(false);
    const [buttonLayout, setButtonLayout] = useState({ x: 0, y: 0, width: 0, height: 0 });
    const buttonRef = useRef<any>(null);

    function handleSelect(value: any){
        onSelect(value);
        setMenuOpen(false);
    }
    const getButtonPosition = () => {
        if (buttonRef.current) {
            // @ts-ignore
            buttonRef.current.measure((fx, fy, width, height, px, py) => {
                setButtonLayout({
                    x: px,
                    y: py,
                    width,
                    height
                });
            });
        }
    };

    const styles = StyleSheet.create({
        dropDownContainer: {
            position: 'absolute',
            overflow: 'hidden',
            width: buttonLayout.width,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownItem: {
            paddingVertical: 10,
            paddingHorizontal: 14,
            width: '100%',
            alignItems: 'center',
            justifyContent: 'center',
            borderRadius: 40,
            borderColor: "#737373",
            borderWidth: 1,
        },
        dropDownItemActive: {
            backgroundColor: '#B2C9FF'
        },
        buttonContainer: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
            width: '100%',
            minWidth:120, // How to make it responsive?
            maxWidth:200,
            //backgroundColor: '#dbe3ff',
            paddingHorizontal: 20,
            paddingVertical: 10,
            borderRadius: 40,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownText:{
            fontSize: 16,
            fontWeight: '300',
            color: "#8400ff",
        },
        modalOverlay: {
            flex: 1,
            backgroundColor: 'transparent',
        },
        modalContent: {
            flex: 1,
            position: 'relative',
        },
        wrapper: {
            position: 'relative',
        },
    });
    return (
        <View style={styles.wrapper}>
            <TouchableOpacity ref={buttonRef} style={[statusStyles[selected], styles.buttonContainer]} onPress={() => {
                getButtonPosition();
                setMenuOpen(true);
            }} onLayout={getButtonPosition}>
                <Text style={{
                    color: selected === "Fravær" ? "black" : (statusStyles[selected]?.color || "#000")
                }}>
                    {selected}
                </Text>

            </TouchableOpacity>

            <Modal
                visible={menuOpen}
                transparent={true}
                animationType="fade"
                onRequestClose={() => setMenuOpen(false)}
            >
                <TouchableOpacity
                    style={styles.modalOverlay}
                    activeOpacity={1}
                    onPress={() => setMenuOpen(false)}
                >
                    {menuOpen && (<View style={styles.modalContent}>
                        <View
                            style={[
                                styles.dropDownContainer,
                                {
                                    top: buttonLayout.y + buttonLayout.height + 5,
                                    left: buttonLayout.x,
                                }
                            ]}
                        >
                            {options.map((label) => (
                                <TouchableOpacity
                                    style={[
                                        styles.dropDownItem,
                                        statusStyles[label],
                                        label === selected && styles.dropDownItemActive
                                    ]}
                                    key={label}
                                    onPress={() => {
                                        handleSelect(label);
                                    }
                                    }
                                >
                                    <Text style={{ color: statusStyles[label]?.color || "#000000"}}>{label}</Text>
                                </TouchableOpacity>
                            ))}
                        </View>
                    </View>)}
                </TouchableOpacity>
            </Modal>
        </View>
    );
}
export default DropDownMenuCrosslist
