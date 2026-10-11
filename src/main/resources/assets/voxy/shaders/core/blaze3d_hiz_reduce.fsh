#version 330
#extension GL_ARB_separate_shader_objects : require
uniform sampler2D Sampler0;
layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;
void main() {
    ivec2 size = textureSize(Sampler0, 0);
    ivec2 base = ivec2(gl_FragCoord.xy) * 2;
    float depth = 1.0;
    for (int y = 0; y < 2; y++) for (int x = 0; x < 2; x++) {
        ivec2 p = min(base + ivec2(x, y), size - 1);
        depth = min(depth, texelFetch(Sampler0, p, 0).r);
    }
    fragColor = vec4(depth);
}
