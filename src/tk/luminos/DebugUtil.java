package tk.luminos;

import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_API_ERROR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_APPLICATION_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_DEPRECATION_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_OTHER_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_PERFORMANCE_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_SEVERITY_HIGH_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_SEVERITY_LOW_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_SEVERITY_MEDIUM_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.glDebugMessageCallbackAMD;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SEVERITY_HIGH_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SEVERITY_LOW_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SEVERITY_MEDIUM_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_API_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_APPLICATION_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_OTHER_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_SHADER_COMPILER_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_THIRD_PARTY_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_ERROR_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_OTHER_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_PERFORMANCE_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_PORTABILITY_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.glDebugMessageCallbackARB;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL30.GL_CONTEXT_FLAGS;
import static org.lwjgl.opengl.GL43.GL_CONTEXT_FLAG_DEBUG_BIT;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_HIGH;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_LOW;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_MEDIUM;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_API;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_APPLICATION;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_OTHER;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_SHADER_COMPILER;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_THIRD_PARTY;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_ERROR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_MARKER;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_OTHER;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_PERFORMANCE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_PORTABILITY;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;
import static org.lwjgl.system.APIUtil.apiLog;
import static org.lwjgl.system.APIUtil.apiUnknownToken;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageAMDCallback;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.system.Callback;

/**
 * Creates debugging utility for OpenGL
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class DebugUtil {

	/**
	 * Creates a functional debug callback interface
	 * 
	 * @author Nick Clark
	 * @version 1.0
	 */
    @FunctionalInterface
    public interface DebugCallback {
        void callback(String source, String type, int id, String severity, String message);
    }

    /**
     * Sets up a debug message callback
     * 
     * @param callback		Debugging callback
     * @return				Callback for OpenGL
     */
    public static Callback setupDebugMessageCallback(DebugCallback callback) {

        GLCapabilities caps = GL.getCapabilities();

        if (caps.OpenGL43) {
            apiLog("[GL] Using OpenGL 4.3 for error logging.");
            GLDebugMessageCallback proc = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
                callback.callback(getDebugSource(source), getDebugType(type), id, getDebugSeverity(severity), GLDebugMessageCallback.getMessage(length, message));
            });
            glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
            glDebugMessageCallback(proc, NULL);
            if ((glGetInteger(GL_CONTEXT_FLAGS) & GL_CONTEXT_FLAG_DEBUG_BIT) == 0) {
                apiLog("[GL] Warning: A non-debug context may not produce any debug output.");
                glEnable(GL_DEBUG_OUTPUT);
            }
            return proc;
        }

        if (caps.GL_KHR_debug) {
            apiLog("[GL] Using KHR_debug for error logging.");
            GLDebugMessageCallback proc = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
                callback.callback(getDebugSource(source), getDebugType(type), id, getDebugSeverity(severity), GLDebugMessageCallback.getMessage(length, message));
            });
            glEnable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS);
            KHRDebug.glDebugMessageCallback(proc, NULL);
            if (caps.OpenGL30 && (glGetInteger(GL_CONTEXT_FLAGS) & GL_CONTEXT_FLAG_DEBUG_BIT) == 0) {
                apiLog("[GL] Warning: A non-debug context may not produce any debug output.");
                glEnable(GL_DEBUG_OUTPUT);
            }
            return proc;
        }

        if (caps.GL_ARB_debug_output) {
            apiLog("[GL] Using ARB_debug_output for error logging.");
            GLDebugMessageARBCallback proc = GLDebugMessageARBCallback.create((source, type, id, severity, length, message, userParam) -> {
                callback.callback(getSourceARB(source), getTypeARB(type), id, getSeverityARB(severity), GLDebugMessageARBCallback.getMessage(length, message));
            });
            glEnable(ARBDebugOutput.GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB);
            glDebugMessageCallbackARB(proc, NULL);
            return proc;
        }

        if (caps.GL_AMD_debug_output) {
            apiLog("[GL] Using AMD_debug_output for error logging.");
            GLDebugMessageAMDCallback proc = GLDebugMessageAMDCallback.create((id, category, severity, length, message, userParam) -> {
                callback.callback(getCategoryAMD(category), "", id, getSeverityAMD(severity), GLDebugMessageAMDCallback.getMessage(length, message));
            });
            glDebugMessageCallbackAMD(proc, NULL);
            return proc;
        }

        apiLog("[GL] No debug output implementation is available.");
        return null;
    }



    private static String getDebugSource(int source) {
        switch (source) {
        case GL_DEBUG_SOURCE_API:
            return "API";
        case GL_DEBUG_SOURCE_WINDOW_SYSTEM:
            return "WINDOW SYSTEM";
        case GL_DEBUG_SOURCE_SHADER_COMPILER:
            return "SHADER COMPILER";
        case GL_DEBUG_SOURCE_THIRD_PARTY:
            return "THIRD PARTY";
        case GL_DEBUG_SOURCE_APPLICATION:
            return "APPLICATION";
        case GL_DEBUG_SOURCE_OTHER:
            return "OTHER";
        default:
            return apiUnknownToken(source);
        }

    }

    private static String getDebugType(int type) {
        switch (type) {
        case GL_DEBUG_TYPE_ERROR:
            return "ERROR";
        case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
            return "DEPRECATED BEHAVIOR";
        case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
            return "UNDEFINED BEHAVIOR";
        case GL_DEBUG_TYPE_PORTABILITY:
            return "PORTABILITY";
        case GL_DEBUG_TYPE_PERFORMANCE:
            return "PERFORMANCE";
        case GL_DEBUG_TYPE_OTHER:
            return "OTHER";
        case GL_DEBUG_TYPE_MARKER:
            return "MARKER";
        default:
            return apiUnknownToken(type);
        }

    }

    private static String getDebugSeverity(int severity) {
        switch (severity) {
        case GL_DEBUG_SEVERITY_HIGH:
            return "HIGH";
        case GL_DEBUG_SEVERITY_MEDIUM:
            return "MEDIUM";
        case GL_DEBUG_SEVERITY_LOW:
            return "LOW";
        case GL_DEBUG_SEVERITY_NOTIFICATION:
            return "NOTIFICATION";
        default:
            return apiUnknownToken(severity);
        }

    }



    private static String getSourceARB(int source) {
        switch (source) {
        case GL_DEBUG_SOURCE_API_ARB:
            return "API";
        case GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB:
            return "WINDOW SYSTEM";
        case GL_DEBUG_SOURCE_SHADER_COMPILER_ARB:
            return "SHADER COMPILER";
        case GL_DEBUG_SOURCE_THIRD_PARTY_ARB:
            return "THIRD PARTY";
        case GL_DEBUG_SOURCE_APPLICATION_ARB:
            return "APPLICATION";
        case GL_DEBUG_SOURCE_OTHER_ARB:
            return "OTHER";
        default:
            return apiUnknownToken(source);
        }

    }

    private static String getTypeARB(int type) {
        switch (type) {
        case GL_DEBUG_TYPE_ERROR_ARB:
            return "ERROR";
        case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB:
            return "DEPRECATED BEHAVIOR";
        case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB:
            return "UNDEFINED BEHAVIOR";
        case GL_DEBUG_TYPE_PORTABILITY_ARB:
            return "PORTABILITY";
        case GL_DEBUG_TYPE_PERFORMANCE_ARB:
            return "PERFORMANCE";
        case GL_DEBUG_TYPE_OTHER_ARB:
            return "OTHER";
        default:
            return apiUnknownToken(type);

        }

    }

    private static String getSeverityARB(int severity) {
        switch (severity) {
        case GL_DEBUG_SEVERITY_HIGH_ARB:
            return "HIGH";
        case GL_DEBUG_SEVERITY_MEDIUM_ARB:
            return "MEDIUM";
        case GL_DEBUG_SEVERITY_LOW_ARB:
            return "LOW";
        default:
            return apiUnknownToken(severity);
        }

    }



    private static String getCategoryAMD(int category) {
        switch (category) {
        case GL_DEBUG_CATEGORY_API_ERROR_AMD:
            return "API ERROR";
        case GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD:
            return "WINDOW SYSTEM";
        case GL_DEBUG_CATEGORY_DEPRECATION_AMD:
            return "DEPRECATION";
        case GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD:
            return "UNDEFINED BEHAVIOR";
        case GL_DEBUG_CATEGORY_PERFORMANCE_AMD:
            return "PERFORMANCE";
        case GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD:
            return "SHADER COMPILER";
        case GL_DEBUG_CATEGORY_APPLICATION_AMD:
            return "APPLICATION";
        case GL_DEBUG_CATEGORY_OTHER_AMD:
            return "OTHER";
        default:
            return apiUnknownToken(category);
        }

    }

    private static String getSeverityAMD(int severity) {
        switch (severity) {
        case GL_DEBUG_SEVERITY_HIGH_AMD:
            return "HIGH";
        case GL_DEBUG_SEVERITY_MEDIUM_AMD:
            return "MEDIUM";
        case GL_DEBUG_SEVERITY_LOW_AMD:
            return "LOW";
        default:
            return apiUnknownToken(severity);

        }

    }

}