#version 120

uniform sampler2D tex;
uniform sampler2D maskLeft;
uniform sampler2D maskRight;
uniform sampler2D maskTop;

uniform vec3 lightLeft;
uniform vec3 lightRight;
uniform vec3 lightTop;

uniform float brightness;

uniform vec3 spriteColor;
uniform bool useColor;

varying vec2 vTex;

void main() {
    vec4 base = texture2D(tex, vTex);
    if (base.a == 0.0) discard;

    // --- couleur sprite ---
    vec3 colorMul = useColor ? spriteColor : vec3(1.0);

    float mL = texture2D(maskLeft, vTex).a;
    float mR = texture2D(maskRight, vTex).a;
    float mT = texture2D(maskTop, vTex).a;

    float count = 0.0;
    vec3 lightSum = vec3(0.0);

    if (mL > 0.0) { count += 1.0; lightSum += lightLeft; }
    if (mR > 0.0) { count += 1.0; lightSum += lightRight; }
    if (mT > 0.0) { count += 1.0; lightSum += lightTop; }

    // 🔑 aucun lighting → couleur seule
    if (count == 0.0 || brightness <= 0.0) {
        gl_FragColor = vec4(base.rgb * colorMul, base.a);
        return;
    }

    vec3 finalLight = lightSum / count;
    vec3 rgb = base.rgb * colorMul * finalLight * brightness;
    gl_FragColor = vec4(rgb, base.a);
}
