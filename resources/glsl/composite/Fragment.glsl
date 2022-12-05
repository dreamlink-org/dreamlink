#version 450

layout (location = 0) out vec4 frag_color;

in vec2 vert_position;

uniform sampler2D sampler_opaque;
uniform sampler2D sampler_transparent_accumulator;
uniform sampler2D sampler_transparent_reveal;

vec4 blend(vec4 src, vec4 dst) {
    return src * src.a + dst * (1.0 - src.a);
}

float epsilon = 0.001;

void main() {
    vec2 offset = vert_position / 2.0 + 0.5;
    vec4 opaque = texture(sampler_opaque, offset);
    vec4 transparent_accumulator = texture(sampler_transparent_accumulator, offset);
    float transparent_reveal = texture(sampler_transparent_reveal, offset).r;

    vec4 transparent_color = vec4(
        transparent_accumulator.rgb / max(transparent_accumulator.a, epsilon), 
        1.0 - transparent_reveal
    );
    frag_color = blend(transparent_color, opaque);
}