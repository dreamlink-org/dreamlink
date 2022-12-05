#version 450

struct TextureMetaData {
    int texture_unit_id;
    int animation_total_frames;
    int animation_row_frames;
    int animation_start_frame;
    int animation_speed;
};

struct TerrainMetaData {
    int local_light;
    int portal_light;
    int is_hidden;
    int is_affected_by_light;
};

layout (location = 0) out vec4 frag_color;

in vec4 vert_screen_position;
in vec2 vert_texture_offset;
flat in TextureMetaData vert_texture_metadata;
flat in TerrainMetaData vert_terrain_metadata;

uniform float base_light;
uniform float portal_light;
uniform int is_show_hidden;

uniform sampler2D sampler_entity;
uniform sampler2D sampler_zone;
uniform sampler2D sampler_portal;

const int entity_texture_unit = 0;
const int zone_texture_unit = 1;
const int portal_texture_unit = 2;
const float epsilon = 0.0001;

vec4 sample_from_offset(vec2 offset, int unit) {
    switch(unit) {
        case entity_texture_unit:
            return texture(sampler_entity, offset);
        case zone_texture_unit:
            return texture(sampler_zone, offset);
        case portal_texture_unit:
            vec2 screen_offset = vert_screen_position.xy / vert_screen_position.w * 0.5 + 0.5;
            return texture(sampler_portal, screen_offset);
        default:
            return vec4(1.0, 0.0, 0.0, 1.0);
    }
};

void main() {
    float total_light = base_light;
    total_light = max(total_light, float(vert_terrain_metadata.local_light) / 15.0);
    total_light = max(total_light, float(vert_terrain_metadata.portal_light) / 15.0 * portal_light);

    frag_color = sample_from_offset(vert_texture_offset, vert_texture_metadata.texture_unit_id);
    if(vert_terrain_metadata.is_affected_by_light == 1) {
        frag_color.rgb *= total_light;
    }

    if(vert_terrain_metadata.is_hidden == 1 && is_show_hidden == 0) {
        discard;
    }

    if(frag_color.a < epsilon) {
        discard;
    } else {
        frag_color.a = 1.0;
    }
};
