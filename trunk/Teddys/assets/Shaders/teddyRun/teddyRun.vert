varying vec2 texCoord;
uniform int m_SelectedTile;
uniform int m_MaxTilesX;
uniform int m_MaxTilesY;
uniform bool m_Reverse;
uniform bool m_Mirrored;
uniform float m_Speed;

uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Time;
attribute vec3 inPosition;
attribute vec2 inTexCoord;


void main() { 

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    texCoord = inTexCoord;

    float tileSelector;
    float curTile = float(m_SelectedTile);

    if(m_Mirrored) {
        curTile = float(m_MaxTilesX) - 1.0 - curTile;
    }

    // switch through view direction on X-Axis
    if(curTile < 1.0) {
        tileSelector = curTile;
    } else {
        tileSelector = curTile / float(m_MaxTilesX);
    }
    
    texCoord.s = texCoord.s / float(m_MaxTilesX) + tileSelector;
    
    if(m_Mirrored) {
        texCoord.s = 1.0 - texCoord.s;
    }

    // switch through run animation on Y-Axis, time dependent
    float curAnim = 0.0;

    if(m_Reverse) {
        curAnim += g_Time * m_Speed;
    } else {
        curAnim -= g_Time * m_Speed;
    }

    //texCoord.t = texCoord.t / float(m_MaxTilesY) + curAnim / float(m_MaxTilesY);
    texCoord.t = texCoord.t / float(m_MaxTilesY) + (floor(mod(curAnim, float(m_MaxTilesY))) / float(m_MaxTilesY));
}