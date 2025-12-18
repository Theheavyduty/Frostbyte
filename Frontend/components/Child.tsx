import { router } from "expo-router";
import {Pressable, StyleSheet, Text, View, ViewStyle} from "react-native";
import {ParentData} from "@/types/parent";
import {ChildData} from "@/types/child";
import FontAwesome from "@expo/vector-icons/FontAwesome";
import AntDesign from "@expo/vector-icons/AntDesign";
import Entypo from '@expo/vector-icons/Entypo';
export type ChildProps = {
    childData: ChildData;
    customStyle?: ViewStyle;
};

export default function Child({ childData,customStyle }: ChildProps) {
    const handlePress = () => {
        const childId = String(childData.id);
        router.push(`/child-details/${childId}`);
    };
    return (
        <Pressable
            onPress={handlePress}

        >
            <View style={[styles.itemContainer, customStyle]}>
                <View style={{display:'flex', flexDirection:'row',alignItems:'center',gap:10}}>
                    <Text style={{fontSize:30}}>👶</Text>
                    <View style={{gap:8}}>
                        <Text style={styles.text}>{childData.name}</Text>
                        <Text style={styles.lighterText}>
                            {childData.birthday}
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