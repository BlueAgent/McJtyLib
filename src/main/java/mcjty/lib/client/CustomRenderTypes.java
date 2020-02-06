package mcjty.lib.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class CustomRenderTypes extends RenderType {

    // Dummy
    public CustomRenderTypes(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable setup, Runnable clear) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, setup, clear);
    }

//    public static final VertexFormat POSITION_COLOR_LIGHTMAP_NORMAL;
//
//    static {
//        POSITION_COLOR_LIGHTMAP_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
//                .add(POSITION_3F).add(COLOR_4UB).add(TEX_2SB).add(NORMAL_3B)
//                .build());
//    }


    private static final LineState THICK_LINES = new LineState(OptionalDouble.of(2.0D));

    public static final RenderType OVERLAY_LINES = get("overlay_lines",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
            RenderType.State.builder().line(THICK_LINES)
                    .layer(PROJECTION_LAYERING)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(DEPTH_ALWAYS)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));

    public static final RenderType QUADS_NOTEXTURE = get("quads_notexture",
            DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 2097152, true, false,
            RenderType.State.builder()
                    .texture(NO_TEXTURE)
                    .shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED)
                    .build(false));

    public static final RenderType LINES_LIGHTMAP = get("lines_lightmap",
            DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_LINES, 256, true, false,
            RenderType.State.builder()
                    .line(new LineState(OptionalDouble.of(1.0)))
                    .texture(NO_TEXTURE)
                    .shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED)
                    .build(false));
}
