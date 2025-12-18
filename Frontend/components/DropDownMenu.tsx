// I will do this with "avdeling" dummy data, but need to discuss other sorting options.
import {useRef, useState} from "react";
import {Modal, TouchableOpacity, View} from "react-native";
import { StyleSheet,Text } from "react-native";


 export const DropDownMenu = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const [selected, setSelected] = useState("Avdeling");
    const [buttonLayout, setButtonLayout] = useState({ x: 0, y: 0, width: 0, height: 0 });
    const buttonRef = useRef<any>(null);

    //Here it needs to be some real data, with option to pass data in component if we use this dropdown meny some other place.
    const options = ["Sommerfugl","Regnbue","Skog","Byen"];

    function handleSelect(value: any){
        setSelected(value);
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
            backgroundColor: 'white',
            borderRadius: 10,
            overflow: 'hidden',
            width: buttonLayout.width,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownItem: {
            padding: 16,
            width: '100%',
            alignItems: 'center',
            justifyContent: 'center',
        },
        dropDownItemActive: {
            backgroundColor: '#cad8ff'
        },
        button: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
            width: '100%',
            minWidth:130, // How to make it responsive?
            maxWidth:200,
            backgroundColor: '#dbe3ff',
            paddingHorizontal: 20,
            paddingVertical: 10,
            borderRadius: 50,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownText:{
            fontSize: 16,
            fontWeight: '300',
            color:'gray'
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
            <TouchableOpacity ref={buttonRef} style={styles.button} onPress={() => {
                getButtonPosition();
                setMenuOpen(true);
            }} onLayout={getButtonPosition}>
                <Text style={styles.dropDownText}>{selected}</Text>

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
                    <View style={styles.modalContent}>
                        <View
                            style={[
                                styles.dropDownContainer,
                                {
                                    top: buttonLayout.y + buttonLayout.height + 5,
                                    left: buttonLayout.x,
                                }
                            ]}
                        >
                            {options.map((option) => (
                                <TouchableOpacity
                                    style={[
                                        styles.dropDownItem,
                                        option === selected && styles.dropDownItemActive
                                    ]}
                                    key={option}
                                    onPress={() => handleSelect(option)}
                                >
                                    <Text style={styles.dropDownText}>{option}</Text>
                                </TouchableOpacity>
                            ))}
                        </View>
                    </View>
                </TouchableOpacity>
            </Modal>
        </View>
    );
    
    
}

