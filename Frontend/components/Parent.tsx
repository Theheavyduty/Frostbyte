import { router } from "expo-router";
import {Pressable, StyleSheet, Text, View, ViewStyle} from "react-native";
import {ParentData} from "@/types/parent";
import FontAwesome from "@expo/vector-icons/FontAwesome";
import AntDesign from '@expo/vector-icons/AntDesign';

export type ParentProps = {
    parentData: ParentData;
    customStyle?: ViewStyle;
};

export default function Parent({ parentData,customStyle }: ParentProps) {
    const handlePress = () => {
        console.log("Parent ID from Parent.tsx:", parentData.id);
        const parentId = String(parentData.id);
        console.log("Converted to string:", parentId);
        router.push(`/parent-details/${parentId}`);
    };
    return (
        <Pressable
            onPress={handlePress}

        >
            <View style={[styles.itemContainer, customStyle]}>
                <View style={{display:'flex', flexDirection:'row',alignItems:'center',gap:10}}>
                    <Text style={{fontSize:30}}>🧑‍🦱</Text>
                    <View style={{gap:8}}>
                        <Text style={styles.text}>{parentData.name}</Text>
                        <Text style={styles.lighterText}>
                            <FontAwesome name="phone" size={14} color="gray" /> {parentData.phoneNumber}

                        </Text>
                    </View>
                </View>
                <AntDesign name="arrow-right" size={18} color="gray" />
            </View>
        </Pressable>
    );
}

const styles = StyleSheet.create({
    itemContainer: {
        flexDirection: 'row',
        backgroundColor:'white',
        width:'100%',
        borderRadius:10,
        paddingVertical:16,
        paddingHorizontal:16,
        justifyContent:'space-between',
        alignItems:'center',
        marginTop:10,
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    text: {
        fontSize: 16,
        fontWeight:'300',
    },

    lighterText:{
        fontSize:16,
        fontWeight:'200',
    }

});